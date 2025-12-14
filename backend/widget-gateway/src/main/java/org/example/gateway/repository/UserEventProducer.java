package org.example.gateway.repository;

import org.example.gateway.utils.JsonUtils;
import org.example.shared.models.events.WidgetEvent;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserEventProducer {

  private final StreamOperations<String, String, String> streamOps;
  private final JsonUtils jsonUtils;

  public UserEventProducer(RedisTemplate<String, String> redisTemplate , JsonUtils jsonUtils) {
    this.streamOps = redisTemplate.opsForStream();
    this.jsonUtils = jsonUtils;
  }

  public void publish(WidgetEvent event) {

    Map<String, String> payload = new HashMap<>();
    if (event.actionType() != null){
    payload.put("userId", event.userId());
    payload.put("widgetId", event.widgetId());
    payload.put("widgetType", event.widgetType());
    payload.put("actionType", event.actionType().name());
    payload.put("timestamp", Instant.now().toString());
    payload.put("metadata", jsonUtils.toJson(event.metadata()));
    streamOps.add(StreamRecords.mapBacked(payload).withStreamKey("widget-events"));}

    System.out.println(jsonUtils.toJson(event));
  }
}
