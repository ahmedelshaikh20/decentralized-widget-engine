package org.example.widgetprocessor.service.aiservice;

import java.util.List;
import java.util.Map;

public interface AiWidgetContentService {
  Map<String, Object> generateWidgetContent(String widgetId, List<Map<String, Object>> lastEvents);
}
