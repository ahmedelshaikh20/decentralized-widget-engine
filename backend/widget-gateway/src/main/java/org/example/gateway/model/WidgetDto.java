package org.example.gateway.model;

public record WidgetDto(
    String id,
    String componentType,
    WidgetData data
) {}
