package org.example.gateway.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shared.models.WidgetCacheDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    List<WidgetCacheDto> merged = new ArrayList<>();

    // 1 Load personalized widgets
    Set<String> personalIds = redis.opsForSet().members(indexKey(userId));
    if (personalIds != null) {
      personalIds.forEach(id -> {
        String json = redis.opsForValue().get(widgetKey(userId, id));
        if (json != null) {
          try {
            merged.add(mapper.readValue(json, WidgetCacheDto.class));
          } catch (Exception ignored) {}
        }
      });
    }

    // 2️⃣ Load default widgets from one JSON list
    String defaultJson = redis.opsForValue().get("widgets:defaults");
    if (defaultJson != null) {
      try {
        List<WidgetCacheDto> defaults =
          mapper.readValue(defaultJson, new TypeReference<List<WidgetCacheDto>>() {});

        Set<String> personalizedIds =
          merged.stream().map(WidgetCacheDto::id).collect(Collectors.toSet());

        defaults.stream()
          .filter(d -> !personalizedIds.contains(d.id()))
          .forEach(merged::add);

      } catch (Exception ignored) {}
    }

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
