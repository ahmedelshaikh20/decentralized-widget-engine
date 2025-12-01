package org.example.widgetprocessor.service.widgetservice;

import org.example.shared.models.WidgetCacheDto;
import org.example.shared.models.WidgetData;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.example.widgetprocessor.model.widgetrule.PersonalizationConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WidgetBuilderServiceImpl implements WidgetBuilderService {

    @Override
    public WidgetCacheDto build(UserEvent event, WidgetRule rule) {

        WidgetData data = buildWidgetData(event, rule.getPersonalization());

        return new WidgetCacheDto(
                rule.getId(),
                rule.getProductId(),
                rule.getComponentType(),
                rule.getPriority(),
                rule.getPlatformVisibility(),
                rule.getTtlSeconds(),
                data
        );
    }

  private WidgetData buildWidgetData(UserEvent event, PersonalizationConfig personalization) {
    if (personalization == null) {
      return new WidgetData(null, null, null, null);
    }

    String title = renderTemplate(personalization.getTitleTemplate(), event);
    String subtitle = renderTemplate(personalization.getSubtitleTemplate(), event);
    String ctaLabel = renderTemplate(personalization.getCtaLabel(), event);
    String ctaUrl = renderTemplate(personalization.getCtaUrl(), event);

    return new WidgetData(title, subtitle, ctaLabel, ctaUrl);
  }


    private String renderTemplate(String template, UserEvent event) {
        if (template == null) return null;

        String result = template;

        for (Map.Entry<String, Object> e : event.getMetadata().entrySet()) {
            String key = "{{" + e.getKey() + "}}";
            result = result.replace(key, e.getValue().toString());
        }

        return result;
    }
}
