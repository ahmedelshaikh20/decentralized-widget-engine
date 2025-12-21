package org.example.widgetprocessor.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.widgetprocessor.config.ProcessorConfig;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.service.widgetprocessor.UserEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Consumes user events from products (flight searches, insurance quotes, etc.)
 * Triggers rule-based widget personalization
 */
@Component
public class UserEventConsumer extends BaseStreamConsumer {

  private static final Logger log = LoggerFactory.getLogger(UserEventConsumer.class);

  private final ObjectMapper objectMapper;
  private final UserEventProcessor userEventProcessor;

  public UserEventConsumer(
    StringRedisTemplate redis,
    ProcessorConfig config,
    @Qualifier("jsonObjectMapper") ObjectMapper objectMapper,
    UserEventProcessor userEventProcessor
  ) {
    super(
      redis,
      config.getStreams().get("user-events").getKey(),
      config.getStreams().get("user-events").getConsumerGroup(),
      config.getStreams().get("user-events").getConsumerName(),
      config.getStreams().get("user-events").getBatchSize(),
      config.getStreams().get("user-events").getPollIntervalMs()
    );
    this.objectMapper = objectMapper;
    this.userEventProcessor = userEventProcessor;
  }

  @Scheduled(fixedDelayString = "${processor.streams.user-events.poll-interval-ms}")
  public void poll() {
    consume();
  }

  @Override
  protected void handleRecord(MapRecord<String, Object, Object> record) throws Exception {
    UserEvent event = convertToUserEvent(record);
    userEventProcessor.processEvent(event);
  }

  private UserEvent convertToUserEvent(MapRecord<String, Object, Object> record) throws PermanentException {

    String userId = str(record, "userId");
    String eventType = str(record, "eventType");
    String productId = str(record, "productId");


    // Validation
    if (userId == null || userId.isBlank()) {
      throw new PermanentException("Missing userId");
    }
    if (eventType == null || eventType.isBlank()) {
      throw new PermanentException("Missing eventType");
    }
    if (productId == null || productId.isBlank()) {
      throw new PermanentException("Missing productId");
    }

    Map<String, Object> metadata = parseJsonMap(str(record, "metadata"));
    long timestamp = longVal(record, "timestamp", System.currentTimeMillis());

    return new UserEvent(
      userId,
      eventType.toUpperCase(Locale.ROOT),
      productId,
      metadata,
      timestamp
    );
  }

  private Map<String, Object> parseJsonMap(String raw) {
    if (raw == null || raw.isBlank()) {
      return new HashMap<>();
    }
    try {
      return objectMapper.readValue(raw, Map.class);
    } catch (Exception e) {
      log.warn("Invalid metadata JSON: {}", raw);
      return new HashMap<>();
    }
  }
}
