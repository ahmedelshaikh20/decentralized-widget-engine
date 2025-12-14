package org.example.widgetprocessor.model.userevent;

import java.util.Map;


// This is user events for handling user events comming from redis
public class UserEvent {

    private String userId;
    private String eventType;
    private String productId;
    private Map<String, Object> metadata;
    private long timestamp;

    public UserEvent() {
        // default constructor for Jackson
    }

    public UserEvent(String userId, String eventType, String productId, Map<String, Object> metadata, long timestamp) {
        this.userId = userId;
        this.eventType = eventType;
        this.productId = productId;
        this.metadata = metadata;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId='" + userId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", productId='" + productId + '\'' +
                ", metadata=" + metadata +
                ", timestamp=" + timestamp +
                '}';
    }
}
