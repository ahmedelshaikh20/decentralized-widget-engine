package org.example.widgetprocessor.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shared.models.events.WidgetEvent;
import org.example.shared.models.events.WidgetEventType;
import org.example.widgetprocessor.config.ProcessorConfig;
import org.example.widgetprocessor.service.analytics.WidgetAnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Consumes widget interaction events from frontend (impressions, clicks)
 * Processes analytics and triggers AI-powered personalization
 */
@Component
public class WidgetEventConsumer extends BaseStreamConsumer {

    private static final Logger log = LoggerFactory.getLogger(WidgetEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final WidgetAnalyticsService analyticsService;

    public WidgetEventConsumer(
        StringRedisTemplate redis,
        ProcessorConfig config,
        @Qualifier("jsonObjectMapper") ObjectMapper objectMapper,
        WidgetAnalyticsService analyticsService
    ) {
        super(
            redis,
            config.getStreams().get("widget-events").getKey(),
            config.getStreams().get("widget-events").getConsumerGroup(),
            config.getStreams().get("widget-events").getConsumerName(),
            config.getStreams().get("widget-events").getBatchSize(),
            config.getStreams().get("widget-events").getPollIntervalMs()
        );
        this.objectMapper = objectMapper;
        this.analyticsService = analyticsService;
    }

    @Scheduled(fixedDelayString = "${processor.streams.widget-events.poll-interval-ms}")
    public void poll() {
        consume();
    }

    @Override
    protected void handleRecord(MapRecord<String, Object, Object> record) throws Exception {
        WidgetEvent event = convertToWidgetEvent(record);

        log.debug("Processing widget event: userId={}, widgetId={}, action={}",
            event.userId(), event.widgetId(), event.actionType());

        analyticsService.processWidgetInteraction(event);
    }

    private WidgetEvent convertToWidgetEvent(MapRecord<String, Object, Object> record) throws PermanentException {
        String userId = str(record, "userId");
        String widgetId = str(record, "widgetId");
        String widgetType = str(record, "widgetType");
        String actionTypeStr = str(record, "actionType");

        // Validation
        if (userId == null || userId.isBlank()) {
            throw new PermanentException("Missing userId");
        }
        if (widgetId == null || widgetId.isBlank()) {
            throw new PermanentException("Missing widgetId");
        }
        if (actionTypeStr == null || actionTypeStr.isBlank()) {
            throw new PermanentException("Missing actionType");
        }

        WidgetEventType actionType;
        try {
            actionType = WidgetEventType.valueOf(actionTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PermanentException("Invalid actionType: " + actionTypeStr);
        }

        Map<String, Object> metadata = parseJsonMap(str(record, "metadata"));

        return new WidgetEvent(userId, widgetId, widgetType, actionType, metadata);
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
