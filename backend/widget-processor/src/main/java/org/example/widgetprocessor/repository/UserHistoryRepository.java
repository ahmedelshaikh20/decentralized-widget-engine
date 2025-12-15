package org.example.widgetprocessor.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserHistoryRepository {

  private final StringRedisTemplate redis;
  private final ObjectMapper mapper;

  public UserHistoryRepository(
    StringRedisTemplate redis,
    @Qualifier("jsonObjectMapper") ObjectMapper mapper
  ) {
    this.redis = redis;
    this.mapper = mapper;
  }

  // Store last 10 events
  public void pushEvent(String userId, Map<String, Object> eventMap) {
    String key = "user-history:" + userId;
    try {
      String json = mapper.writeValueAsString(eventMap);
      redis.opsForList().leftPush(key, json);
      redis.opsForList().trim(key, 0, 9);
    } catch (Exception ignored) {}
  }

  // Store last metadata for each event type
  public void updateLastMetadata(String userId, String eventType, Map<String, Object> metadata) {
    try {
      String key = "user-meta:" + userId;
      redis.opsForHash().put(key, "LAST_" + eventType, mapper.writeValueAsString(metadata));
    } catch (Exception ignored) {}
  }

  public List<Map<String, Object>> getLastEvents(String userId) {
    String key = "user-history:" + userId;

    List<String> rawMap = redis.opsForList().range(key, 0, 9);
    if (rawMap == null) return List.of();

    return rawMap.stream().map(r -> {
      try {
        return mapper.readValue(r, new TypeReference<>() {});
      } catch (Exception ignored) {
        return Map.<String, Object>of();
      }
    }).toList();
  }

  // Get last metadata for an event type
  public Map<String, Object> getLastMetadata(String userId, String eventType) {
    try {
      String field = "LAST_" + eventType;
      String raw = (String) redis.opsForHash().get("user-meta:" + userId, field);
      if (raw == null) return Map.of();
      return mapper.readValue(raw, Map.class);
    } catch (Exception e) {
      return Map.of();
    }
  }
}
