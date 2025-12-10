package org.example.widgetprocessor.service.widgetservice;

import org.example.shared.models.CarouselItem;
import org.example.shared.models.WidgetCacheDto;
import org.example.shared.models.WidgetData;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.example.widgetprocessor.model.widgetrule.PersonalizationConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WidgetBuilderServiceImpl implements WidgetBuilderService {

  @Override
  public WidgetCacheDto build(UserEvent event, WidgetRule rule) {

    WidgetData data = buildWidgetData(event, rule.getPersonalization(), rule);



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

  private WidgetData buildWidgetData(UserEvent event,
                                     PersonalizationConfig personalization,
                                     WidgetRule rule) {

    if (personalization == null) {
      return emptyData();
    }

    String title = renderTemplate(personalization.getTitleTemplate(), event);
    String subtitle = renderTemplate(personalization.getSubtitleTemplate(), event);
    String ctaLabel = renderTemplate(personalization.getCtaLabel(), event);
    String ctaUrl = renderTemplate(personalization.getCtaUrl(), event);

    String componentType = rule.getComponentType().toLowerCase();
    Map<String, Object> metadata = event.getMetadata();

    String imageUrl = null;
    String iconUrl = null;
    List<CarouselItem> items = null;

    if (componentType.equals("banner")) {
      Map<String, Object> hero = (Map<String, Object>) metadata.get("hero");
      if (hero != null) {
        imageUrl = safeString(hero.get("imageUrl"));
      }
    }

    if (componentType.equals("card")) {
      iconUrl = safeString(metadata.get("iconUrl"));
    }

    if (componentType.equals("carousel")) {
      List<Map<String, Object>> deals = (List<Map<String, Object>>) metadata.getOrDefault("deals", null);
      items = buildCarouselItems(deals);
    }

    return new WidgetData(
      title,
      subtitle,
      imageUrl,
      iconUrl,
      items,
      ctaLabel,
      ctaUrl
    );
  }

  private List<CarouselItem> buildCarouselItems(List<Map<String, Object>> deals) {
    if (deals == null) return null;

    List<CarouselItem> list = new ArrayList<>();
    for (Map<String, Object> d : deals) {
      list.add(new CarouselItem(
        safeString(d.get("title")),
        safeString(d.get("imageUrl")),
        safeString(d.get("ctaLabel")),
        safeString(d.get("ctaUrl"))
      ));
    }
    return list;
  }

  private String renderTemplate(String template, UserEvent event) {
    if (template == null) return null;

    String result = template;
    for (Map.Entry<String, Object> e : event.getMetadata().entrySet()) {
      String key = "{{" + e.getKey() + "}}";
      result = result.replace(key, safeString(e.getValue()));
    }
    return result;
  }

  private String safeString(Object o) {
    return (o == null) ? null : o.toString();
  }

  private WidgetData emptyData() {
    return new WidgetData(null, null, null, null, null, null, null);
  }
}
