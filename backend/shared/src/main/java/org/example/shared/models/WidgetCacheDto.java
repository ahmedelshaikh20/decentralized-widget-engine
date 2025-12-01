package org.example.shared.models;


import java.util.List;

public record WidgetCacheDto(
    String id,
    String product,
    String componentType,
    int priority,
    List<Platform> platformVisibility,
    int ttlSeconds,
    WidgetData data
) {



  public WidgetDto toBusinessModel() {
    return new WidgetDto(
      this.id,
      this.componentType,
      this.data
    );
  }


}




