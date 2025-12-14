package org.example.widgetprocessor.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

/**
 * Base class for Redis Stream consumers
 * Handles connection, consumer group setup, and error handling
 */
public abstract class BaseStreamConsumer {

    private static final Logger log = LoggerFactory.getLogger(BaseStreamConsumer.class);

    protected final StringRedisTemplate redis;
    protected final String streamKey;
    protected final String consumerGroup;
    protected final String consumerName;
    protected final int batchSize;
    protected final Duration blockDuration;

    protected BaseStreamConsumer(
        StringRedisTemplate redis,
        String streamKey,
        String consumerGroup,
        String consumerName,
        int batchSize,
        int blockDurationMs
    ) {
        this.redis = redis;
        this.streamKey = streamKey;
        this.consumerGroup = consumerGroup;
        this.consumerName = consumerName;
        this.batchSize = batchSize;
        this.blockDuration = Duration.ofMillis(blockDurationMs);
    }

    @PostConstruct
    public void initialize() {
        createConsumerGroupIfNotExists();
        log.info("Consumer initialized: stream={}, group={}, consumer={}",
            streamKey, consumerGroup, consumerName);
    }

    private void createConsumerGroupIfNotExists() {
        try {
            redis.opsForStream().createGroup(streamKey, consumerGroup);
            log.info("Created consumer group: {}", consumerGroup);
        } catch (Exception e) {
            log.debug("Consumer group already exists: {}", consumerGroup);
        }
    }

    /**
     * Poll and process messages from the stream
     */
    public void consume() {
        try {
            List<MapRecord<String, Object, Object>> records = redis.opsForStream().read(
                Consumer.from(consumerGroup, consumerName),
                StreamReadOptions.empty()
                    .count(batchSize)
                    .block(blockDuration),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed())
            );

            if (records == null || records.isEmpty()) {
                return;
            }

            log.debug("Received {} records from {}", records.size(), streamKey);

            for (MapRecord<String, Object, Object> record : records) {
                processRecord(record);
            }

        } catch (Exception e) {
            log.error("Failed to consume from stream {}", streamKey, e);
        }
    }

    /**
     * Process a single record with retry logic
     */
    private void processRecord(MapRecord<String, Object, Object> record) {
        RecordId id = record.getId();
        int attempt = 0;
        int maxAttempts = 3;

        while (attempt < maxAttempts) {
            try {
                handleRecord(record);
                acknowledge(id);
                return; // Success

            } catch (PermanentException e) {
                // Business logic error - don't retry
                log.error("Permanent failure for record {}: {}", id, e.getMessage());
                moveToDeadLetterQueue(record);
                acknowledge(id);
                return;

            } catch (Exception e) {
                attempt++;
                if (attempt < maxAttempts) {
                    log.warn("Retry {}/{} for record {}: {}",
                        attempt, maxAttempts, id, e.getMessage());
                    try {
                        Thread.sleep(1000 * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    log.error("Max retries exceeded for record {}", id, e);
                    moveToDeadLetterQueue(record);
                    acknowledge(id);
                }
            }
        }
    }

    /**
     * Handle a single record - implemented by subclasses
     */
    protected abstract void handleRecord(MapRecord<String, Object, Object> record) throws Exception;

    /**
     * Acknowledge processed message
     */
    protected void acknowledge(RecordId id) {
        try {
            redis.opsForStream().acknowledge(streamKey, consumerGroup, id);
            log.debug("Acknowledged record: {}", id);
        } catch (Exception e) {
            log.error("Failed to acknowledge record {}", id, e);
        }
    }

    /**
     * Move failed record to dead letter queue
     */
    protected void moveToDeadLetterQueue(MapRecord<String, Object, Object> record) {
        try {
            String dlqKey = streamKey + ":dlq";
            redis.opsForStream().add(
                StreamRecords.mapBacked(record.getValue())
                    .withStreamKey(dlqKey)
            );
            log.info("Moved record to DLQ: {} -> {}", record.getId(), dlqKey);
        } catch (Exception e) {
            log.error("Failed to move record to DLQ", e);
        }
    }

    /**
     * Helper: Extract string value from record
     */
    protected String str(MapRecord<String, Object, Object> record, String key) {
        Object value = record.getValue().get(key);
        return value == null ? null : value.toString();
    }

    /**
     * Helper: Extract long value from record
     */
    protected long longVal(MapRecord<String, Object, Object> record, String key, long defaultValue) {
        Object value = record.getValue().get(key);
        if (value == null) return defaultValue;
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Exception to indicate permanent failure (don't retry)
     */
    public static class PermanentException extends Exception {
        public PermanentException(String message) {
            super(message);
        }

        public PermanentException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
