package org.example.shared.models;

public record WidgetDto(
    String id,
    String componentType,
    WidgetData data
) {}
