package org.example.gateway.repository;


import org.example.shared.models.Platform;
import org.example.shared.models.WidgetData;
import org.example.shared.models.WidgetDto;

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




