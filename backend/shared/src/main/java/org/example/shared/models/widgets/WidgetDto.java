package org.example.shared.models.widgets;

public record WidgetDto(
  String id,
  String componentType,
  WidgetPayload payload
) {}
