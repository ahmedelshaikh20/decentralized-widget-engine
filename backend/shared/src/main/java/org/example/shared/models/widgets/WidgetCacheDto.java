package org.example.shared.models.widgets;

import java.util.List;

public record WidgetCacheDto(
  String id,
  String product,
  String componentType,
  int priority,
  List<Platform> platformVisibility,
  int ttlSeconds,
  WidgetPayload payload
) {
  public WidgetDto toBusinessModel() {
    return new WidgetDto(this.id, this.componentType, this.payload);
  }
}





