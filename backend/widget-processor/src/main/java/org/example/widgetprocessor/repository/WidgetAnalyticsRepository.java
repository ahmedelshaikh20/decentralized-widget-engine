package org.example.widgetprocessor.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class WidgetAnalyticsRepository {

    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;

    public WidgetAnalyticsRepository(
        StringRedisTemplate redis,
        @Qualifier("jsonObjectMapper") ObjectMapper mapper
    ) {
        this.redis = redis;
        this.mapper = mapper;
    }

    // ============================================================
    // Widget Interaction Storage
    // ============================================================

    /**
     * Store widget event in user's interaction history
     * Keeps last 50 events per user
     */
    public void storeWidgetEvent(String userId, Map<String, Object> eventData) {
        String key = "widget-analytics:" + userId;

        try {
            String json = mapper.writeValueAsString(eventData);
            redis.opsForList().leftPush(key, json);
            redis.opsForList().trim(key, 0, 49); // Keep last 50
        } catch (Exception e) {
            // Log but don't throw - analytics shouldn't break processing
        }
    }

    /**
     * Get recent widget interaction events
     */
    public List<Map<String, Object>> getRecentWidgetEvents(String userId, int count) {
        String key = "widget-analytics:" + userId;

        List<String> raw = redis.opsForList().range(key, 0, count - 1);
        if (raw == null) return List.of();

        return raw.stream()
            .map(this::parseJson)
            .filter(map -> !map.isEmpty())
            .toList();
    }

    // ============================================================
    // Click Counters
    // ============================================================

    /**
     * Increment and return total click count for user
     */
    public long incrementClickCount(String userId, String widgetId) {
        String globalKey = "widget-clicks:" + userId;
        String perWidgetKey = "widget-clicks:" + userId + ":widgets";

        // Increment global counter
        Long count = redis.opsForValue().increment(globalKey);

        // Track per-widget clicks
        redis.opsForHash().increment(perWidgetKey, widgetId, 1);

        return count != null ? count : 0;
    }

    /**
     * Get total click count for user
     */
    public long getUserClickCount(String userId) {
        String key = "widget-clicks:" + userId;
        String count = redis.opsForValue().get(key);
        return count == null ? 0 : Long.parseLong(count);
    }

    // ============================================================
    // Impression Counters
    // ============================================================

    /**
     * Increment impression count for specific widget
     */
    public void incrementImpressionCount(String userId, String widgetId) {
        String key = "widget-impressions:" + userId;
        redis.opsForHash().increment(key, widgetId, 1);
    }

    /**
     * Get impression count for specific widget
     */
    public long getWidgetImpressionCount(String userId, String widgetId) {
        String key = "widget-impressions:" + userId;
        Object count = redis.opsForHash().get(key, widgetId);
        return count == null ? 0 : Long.parseLong(count.toString());
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    private Map<String, Object> parseJson(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return Map.of();
        }
    }
}
