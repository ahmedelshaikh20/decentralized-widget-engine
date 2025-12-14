package org.example.shared.models.widgets;

import java.util.Map;

public record WidgetPayload(
    String schemaVersion,
    Map<String, Object> layout,
    Map<String, Object> content
) {}
