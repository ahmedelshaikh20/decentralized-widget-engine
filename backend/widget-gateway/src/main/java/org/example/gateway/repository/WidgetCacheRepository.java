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

  public WidgetCacheRepository(StringRedisTemplate redis, ObjectMapper mapper) {
    this.redis = redis;
    this.mapper = mapper;
  }

  private String indexKey(String userId) {
    return "widgets:" + userId + ":index";
  }

  private String widgetKey(String userId, String widgetId) {
    return "widgets:" + userId + ":" + widgetId;
  }

  public List<WidgetCacheDto> getWidgetsMerged(String userId) {
    List<WidgetCacheDto> personalized = new ArrayList<>();
    List<WidgetCacheDto> defaults = new ArrayList<>();

    // 1. Load personalized widgets
    Set<String> ids = redis.opsForSet().members(indexKey(userId));
    if (ids != null) {
      ids.forEach(id -> {
        String json = redis.opsForValue().get(widgetKey(userId, id));
        if (json != null) {
          try {
            personalized.add(mapper.readValue(json, WidgetCacheDto.class));
          } catch (Exception ignored) {}
        }
      });
    }

    // 2. Load default widgets
    String defaultJson = redis.opsForValue().get("widgets:defaults");
    if (defaultJson != null) {
      try {
        defaults = mapper.readValue(defaultJson, new TypeReference<List<WidgetCacheDto>>(){});
      } catch (Exception ignored) {}
    }

    // 3. Remove defaults that are overridden by personalized (same widget id)
    Set<String> personalIds = personalized.stream()
      .map(WidgetCacheDto::id)
      .collect(Collectors.toSet());

    List<WidgetCacheDto> filteredDefaults = defaults.stream()
      .filter(d -> !personalIds.contains(d.id()))
      .toList();

    // 4. Merge → personalized FIRST, defaults AFTER
    List<WidgetCacheDto> merged = new ArrayList<>();
    merged.addAll(personalized);
    merged.addAll(filteredDefaults);

    System.out.println(merged.stream().map(WidgetCacheDto::id).collect(Collectors.joining(",")));

    return merged;
  }



  public void deleteWidgetsForUser(String userId) {
    String indexKey = indexKey(userId);
    Set<String> ids = redis.opsForSet().members(indexKey);

    if (ids != null && !ids.isEmpty()) {
      ids.forEach(id -> redis.delete(widgetKey(userId, id)));
    }
    redis.delete(indexKey);
    System.out.println("Cleared personalization for user: " + userId);
  }


}
