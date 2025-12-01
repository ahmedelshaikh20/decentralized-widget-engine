package org.example.widgetprocessor.service.widgetservice;

import org.example.shared.models.WidgetCacheDto;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;

public interface WidgetBuilderService {
    WidgetCacheDto build(UserEvent event, WidgetRule rule);
}
