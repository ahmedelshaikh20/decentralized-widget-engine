package org.example.widgetprocessor.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.widgetprocessor.config.ProcessorConfig;
import org.example.widgetprocessor.model.userevent.EventType;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.service.widgetprocessor.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserEventConsumer {

  private static final Logger log = LoggerFactory.getLogger(UserEventConsumer.class);
  private final StringRedisTemplate redis;
  private final ProcessorConfig config;
  private final ObjectMapper objectMapper;
  private final EventProcessor eventProcessor;

  public UserEventConsumer(StringRedisTemplate redis, ProcessorConfig config, @Qualifier("jsonObjectMapper")  ObjectMapper objectMapper, EventProcessor eventProcessor) {
    this.redis = redis;
    this.config = config;
    this.objectMapper = objectMapper;
    this.eventProcessor = eventProcessor;
    log.info("UserEventConsumer initialized: streamKey={}, group={}, consumer={}",
      config.getKey(), config.getConsumerGroup(), config.getConsumerName());

  }

  @PostConstruct
  public void initialize() {
    createConsumerGroupIfNotExists();
    log.info("UserEventConsumer ready");
  }

  private void createConsumerGroupIfNotExists() {
    try {
      redis.opsForStream().createGroup(config.getKey(), config.getConsumerGroup());
      log.info("Created consumer group: {}", config.getConsumerGroup());
    } catch (Exception e) {
      log.info("Consumer group already exists: {}", config.getConsumerGroup());
    }
  }


  @Scheduled(fixedDelayString = "${processor.stream.poll-interval-ms}")
  public void consume() {
    try {
      List<MapRecord<String, Object, Object>> records = redis.opsForStream().read(
        Consumer.from(config.getConsumerGroup(), config.getConsumerName()),
        StreamReadOptions.empty()
          .count(config.getBatchSize())
          .block(Duration.ofMillis(config.getBlockDurationMs())),
        StreamOffset.create(config.getKey(), ReadOffset.lastConsumed())
      );

      if (records == null || records.isEmpty()) return;

      for (MapRecord<String, Object, Object> record : records) {
        handleRecord(record);
      }

    } catch (Exception e) {
      log.error("Failed to consume events", e);
    }
  }

  private void handleRecord(MapRecord<String, Object, Object> record) {
    RecordId id = record.getId();

    try {
      UserEvent event = convertToUserEvent(record.getValue());
      eventProcessor.processEvent(event);
      acknowledge(id);
    } catch (Exception e) {
      log.error("Failed to process record {}. Will NOT ack.", id, e);
    }
  }

  private UserEvent convertToUserEvent(Map<Object, Object> map) {

    String userId = str(map, "userId");
    String eventType = str(map, "eventType");
    String productId = str(map, "productId");

    if (userId == null || eventType == null || productId == null) {
      throw new IllegalArgumentException("Missing required fields");
    }

    Map<String, Object> metadata = parseJsonMap(str(map, "metadata"));
    long timestamp = longVal(map, "timestamp", System.currentTimeMillis());

    return new UserEvent(userId, EventType.fromString(eventType), productId, metadata, timestamp);
  }

  private void acknowledge(RecordId id) {
    try {
      redis.opsForStream().acknowledge(config.getKey(), config.getConsumerGroup(), id);
    } catch (Exception e) {
      log.error("Failed to ack {}", id, e);
    }
  }




  // To get str value from json recieved
  private String str(Map<Object, Object> map, String key) {
    Object v = map.get(key);
    return v == null ? null : v.toString();
  }
  //to get long
  private long longVal(Map<Object, Object> map, String key, long defaultValue) {
    Object v = map.get(key);
    if (v == null) return defaultValue;
    try {
      return Long.parseLong(v.toString());
    } catch (Exception e) {
      return defaultValue;
    }
  }

  // to parse json map
  private Map<String, Object> parseJsonMap(String raw) {
    if (raw == null || raw.isBlank()) return new HashMap<>();
    try {
      return objectMapper.readValue(raw, Map.class);
    } catch (Exception e) {
      log.warn("Invalid metadata JSON: {}", raw);
      return new HashMap<>();
    }
  }
}

