package org.example.shared.models.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record WidgetEvent(String userId, String widgetId, String widgetType, WidgetEventType actionType,
                          Map<String, Object> metadata) {

  @JsonCreator
  public WidgetEvent(
    @JsonProperty("userId") String userId,
    @JsonProperty("widgetId") String widgetId,
    @JsonProperty("widgetType") String widgetType,
    @JsonProperty("actionType") WidgetEventType actionType,
    @JsonProperty("metadata") Map<String, Object> metadata
  ) {
    this.userId = userId;
    this.widgetId = widgetId;
    this.widgetType = widgetType;
    this.actionType = actionType;
    this.metadata = metadata;
  }
}
