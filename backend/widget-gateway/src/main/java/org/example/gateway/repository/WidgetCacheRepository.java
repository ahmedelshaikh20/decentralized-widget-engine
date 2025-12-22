package org.example.gateway.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shared.models.widgets.WidgetCacheDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class WidgetCacheRepository {

  private final StringRedisTemplate redis;
  private final ObjectMapper mapper;
  private static final String DEFAULTS_KEY = "widgets:defaults";
  public WidgetCacheRepository(StringRedisTemplate redis, ObjectMapper mapper) {
    this.redis = redis;
    this.mapper = mapper;
  }


  private String widgetHashKey(String userId) {
    return "widgets:" + userId + ":hash";
  }

  private String widgetExpKey(String userId) {
    return "widgets:" + userId + ":exp";
  }


  public List<WidgetCacheDto> getWidgetsMerged(String userId) {
    purgeExpired(userId);

    List<WidgetCacheDto> personalized = loadPersonalized(userId);

    List<WidgetCacheDto> defaults = loadDefaults();

    // 3) Remove defaults overridden by personalized (same widget id)
    Set<String> personalIds = personalized.stream()
      .map(WidgetCacheDto::id)
      .collect(Collectors.toSet());

    List<WidgetCacheDto> filteredDefaults = defaults.stream()
      .filter(d -> !personalIds.contains(d.id()))
      .toList();

    // 4) Merge → personalized FIRST, defaults AFTER
    List<WidgetCacheDto> merged = new ArrayList<>(personalized.size() + filteredDefaults.size());
    merged.addAll(personalized);
    merged.addAll(filteredDefaults);

    return merged;
  }


  private List<WidgetCacheDto> loadPersonalized(String userId) {
    List<Object> raw = redis.opsForHash().values(widgetHashKey(userId)); // HVALS
    if (raw == null || raw.isEmpty()) return List.of();

    List<WidgetCacheDto> result = new ArrayList<>(raw.size());
    for (Object obj : raw) {
      if (obj == null) continue;
      String json = obj.toString();
      try {
        result.add(mapper.readValue(json, WidgetCacheDto.class));
      } catch (Exception ignored) {}
    }
    return result;
  }

  private List<WidgetCacheDto> loadDefaults() {
    String defaultJson = redis.opsForValue().get(DEFAULTS_KEY);
    if (defaultJson == null || defaultJson.isBlank()) return List.of();
    try {
      return mapper.readValue(defaultJson, new TypeReference<List<WidgetCacheDto>>() {});
    } catch (Exception ignored) {
      return List.of();
    }
  }

  // remove expired widgets
  private void purgeExpired(String userId) {
    long now = System.currentTimeMillis();
    String expKey = widgetExpKey(userId);
    String hashKey = widgetHashKey(userId);

    Set<String> expiredIds = redis.opsForZSet().rangeByScore(expKey, 0, now);
    if (expiredIds == null || expiredIds.isEmpty()) return;

    redis.opsForHash().delete(hashKey, expiredIds.toArray());
    redis.opsForZSet().remove(expKey, expiredIds.toArray());
  }



  public void deleteWidgetsForUser(String userId) {
    redis.delete(widgetHashKey(userId));
    redis.delete(widgetExpKey(userId));
  }

}
