package org.example.widgetprocessor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "processor")
public class ProcessorConfig {

  private Map<String, StreamProperties> streams;
  private RetryProperties retry;

  // ===== Getters & Setters =====

  public Map<String, StreamProperties> getStreams() {
    return streams;
  }

  public void setStreams(Map<String, StreamProperties> streams) {
    this.streams = streams;
  }

  public RetryProperties getRetry() {
    return retry;
  }

  public void setRetry(RetryProperties retry) {
    this.retry = retry;
  }

  // ===== Nested Classes =====

  public static class StreamProperties {

    private String key;
    private String consumerGroup;
    private String consumerName;
    private int batchSize;
    private int pollIntervalMs;
    private int blockMs;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getConsumerGroup() {
      return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
      this.consumerGroup = consumerGroup;
    }

    public String getConsumerName() {
      return consumerName;
    }

    public void setConsumerName(String consumerName) {
      this.consumerName = consumerName;
    }

    public int getBatchSize() {
      return batchSize;
    }

    public void setBatchSize(int batchSize) {
      this.batchSize = batchSize;
    }

    public int getPollIntervalMs() {
      return pollIntervalMs;
    }

    public void setPollIntervalMs(int pollIntervalMs) {
      this.pollIntervalMs = pollIntervalMs;
    }

    public int getBlockMs() {
      return blockMs;
    }

    public void setBlockMs(int blockMs) {
      this.blockMs = blockMs;
    }
  }

  public static class RetryProperties {

    private int maxAttempts;
    private long backoffMs;

    public int getMaxAttempts() {
      return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
      this.maxAttempts = maxAttempts;
    }

    public long getBackoffMs() {
      return backoffMs;
    }

    public void setBackoffMs(long backoffMs) {
      this.backoffMs = backoffMs;
    }
  }
}
