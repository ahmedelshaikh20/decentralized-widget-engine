package org.example.widgetprocessor.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shared.models.WidgetCacheDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class WidgetCacheWriteRepository {

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public WidgetCacheWriteRepository(StringRedisTemplate redis, @Qualifier("jsonObjectMapper") ObjectMapper mapper) {
        this.redis = redis;
        this.mapper = mapper;
    }

    private String key(String userId, String widgetId) {
        return "widgets:" + userId + ":" + widgetId;
    }

    private String indexKey(String userId) {
        return "widgets:" + userId + ":index";
    }

    public void saveWidget(String userId, WidgetCacheDto widget) {
        try {
            String json = mapper.writeValueAsString(widget);
            String redisKey = key(userId, widget.id());

            redis.opsForValue().set(redisKey, json, widget.ttlSeconds(), TimeUnit.SECONDS);
            redis.opsForSet().add(indexKey(userId), widget.id());

        } catch (Exception e) {
            throw new RuntimeException("Failed to save widget", e);
        }
    }

    public void removeWidget(String userId, String widgetId) {
        redis.delete(key(userId, widgetId));
        redis.opsForSet().remove(indexKey(userId), widgetId);
    }
}
