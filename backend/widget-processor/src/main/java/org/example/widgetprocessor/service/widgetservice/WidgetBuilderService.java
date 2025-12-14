package org.example.widgetprocessor.service.widgetservice;

import org.example.shared.models.widgets.WidgetCacheDto;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;

import java.util.List;
import java.util.Map;

public interface WidgetBuilderService {

  WidgetCacheDto build(
    UserEvent event,
    WidgetRule rule,
    Map<String, Object> lastEventMeta,
    List<Map<String, Object>> history
  );
}
