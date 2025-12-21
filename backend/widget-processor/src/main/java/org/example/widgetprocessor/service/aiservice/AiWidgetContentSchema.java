package org.example.widgetprocessor.service.aiservice;

import java.util.Map;

public record AiWidgetContentSchema(
  String title,
  String subtitle,
  String description,
  Cta cta
) {
  public record Cta(String label, String url) {}
}
