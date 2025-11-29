package org.example.gateway.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class WidgetCacheRepository {

  private final StringRedisTemplate redis;
  private final ObjectMapper mapper;

  public WidgetCacheRepository(StringRedisTemplate redis, ObjectMapper mapper) {
    this.redis = redis;
    this.mapper = mapper;
  }

  public List<WidgetCacheDto> getWidgets(String userId) {
    String key = "widgets:" + userId;

    String json = redis.opsForValue().get(key);
    if (json == null) {
      return List.of();
    }

    try {
      return mapper.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      return List.of();
    }
  }
}
