package org.example.widgetprocessor.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shared.models.widgets.WidgetCacheDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Repository
public class WidgetCacheWriteRepository {

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;
    private static final Duration USER_STRUCT_TTL = Duration.ofHours(24);
    public WidgetCacheWriteRepository(StringRedisTemplate redis, @Qualifier("jsonObjectMapper") ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }


  private String widgetHashKey(String userId) {
    return "widgets:" + userId + ":hash";
  }

  private String widgetExpKey(String userId) {
    return "widgets:" + userId + ":exp";
  }

  public void saveWidget(String userId, WidgetCacheDto widget) {
    try {
      String json = mapper.writeValueAsString(widget);

      String hashKey = widgetHashKey(userId);
      String expKey  = widgetExpKey(userId);

      redis.opsForHash().put(hashKey, widget.id(), json);

      long expireAt = System.currentTimeMillis() + (widget.ttlSeconds() * 1000L);
      redis.opsForZSet().add(expKey, widget.id(), expireAt);

      //This Commented to be used in production this set expireytime = min(widget.ttlSeconds,USER_STRUCT_TTL)
//      redis.expire(hashKey, USER_STRUCT_TTL);
//      redis.expire(expKey, USER_STRUCT_TTL);

    } catch (Exception e) {
      throw new RuntimeException("Failed to save widget", e);
    }
  }

  public void removeWidget(String userId, String widgetId) {
    redis.opsForHash().delete(widgetHashKey(userId), widgetId);
    redis.opsForZSet().remove(widgetExpKey(userId), widgetId);
  }
}
