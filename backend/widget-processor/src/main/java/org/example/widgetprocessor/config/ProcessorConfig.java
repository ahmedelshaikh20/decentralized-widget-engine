package org.example.widgetprocessor.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "processor.stream")
public class ProcessorConfig {

    private String key = "user-events";
    private String consumerGroup = "widget-processor-group";
    private String consumerName = "processor-1";
    private int batchSize = 10;
    private long pollIntervalMs = 100;
    @JsonProperty("block-ms")
    private long blockDurationMs = 1000;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getConsumerGroup() { return consumerGroup; }
    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getConsumerName() { return consumerName; }
    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public long getPollIntervalMs() { return pollIntervalMs; }
    public void setPollIntervalMs(long pollIntervalMs) {
        this.pollIntervalMs = pollIntervalMs;
    }

    public long getBlockDurationMs() { return blockDurationMs; }
    public void setBlockDurationMs(long blockDurationMs) {
        this.blockDurationMs = blockDurationMs;
    }
}
