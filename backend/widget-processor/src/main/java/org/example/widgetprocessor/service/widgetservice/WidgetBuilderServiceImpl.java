package org.example.widgetprocessor.service.widgetservice;

import org.example.shared.models.widgets.WidgetCacheDto;
import org.example.shared.models.widgets.WidgetPayload;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WidgetBuilderServiceImpl implements WidgetBuilderService {

  @Override
  public WidgetCacheDto build(
    UserEvent event,
    WidgetRule rule,
    Map<String, Object> lastEventMeta,
    List<Map<String, Object>> history
  ) {
    // ---------------- Build Context ----------------
    Map<String, Object> context = new HashMap<>();

    // Event metadata
    if (event.getMetadata() != null) {
      context.putAll(event.getMetadata());
    }

    // Last metadata snapshot
    context.put("last", lastEventMeta);

    // History
    context.put("history", history);

    // ---------------- Build Payload ----------------
    WidgetPayload payload = buildPayload(rule, context);
    int finalPriority = calculateFinalPriority(rule, event, history);

    return new WidgetCacheDto(
      rule.getId(),
      rule.getProductId(),
      rule.getComponentType(),
      finalPriority,
      rule.getPlatformVisibility(),
      rule.getTtlSeconds(),
      payload
    );
  }

  // =========================================================
  // Payload Builder (NO UI KNOWLEDGE HERE)
  // =========================================================

  private WidgetPayload buildPayload(
    WidgetRule rule,
    Map<String, Object> ctx
  ) {
    Map<String, Object> content =
      renderTemplateMap(rule.getContentTemplate(), ctx);

    Map<String, Object> layout =
      rule.getLayoutConfig() == null ? Map.of() : rule.getLayoutConfig();

    return new WidgetPayload(rule.getSchemaVersion(), layout, content);
  }

  // =========================================================
  // Recursive Template Rendering
  // =========================================================

  @SuppressWarnings("unchecked")
  private Map<String, Object> renderTemplateMap(
    Map<String, Object> template,
    Map<String, Object> ctx
  ) {
    if (template == null) return Map.of();

    Map<String, Object> result = new HashMap<>();

    for (Map.Entry<String, Object> entry : template.entrySet()) {
      Object value = entry.getValue();

      if (value instanceof String str) {

        // CASE 1: pure placeholder → return object as-is
        if (str.matches("^\\{\\{\\w+}}$")) {
          String key = str.substring(2, str.length() - 2);
          result.put(entry.getKey(), ctx.get(key));
        }
        // CASE 2: mixed string → interpolate
        else {
          result.put(entry.getKey(), renderTemplate(str, ctx));
        }
      }
      else if (value instanceof Map<?, ?> map) {
        result.put(
          entry.getKey(),
          renderTemplateMap((Map<String, Object>) map, ctx)
        );

      } else if (value instanceof List<?> list) {
        result.put(
          entry.getKey(),
          renderTemplateList((List<Object>) list, ctx)
        );

      } else {
        // primitives, objects → pass through untouched
        result.put(entry.getKey(), value);
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private List<Object> renderTemplateList(
    List<Object> list,
    Map<String, Object> ctx
  ) {
    return list.stream().map(item -> {
      if (item instanceof String str) {
        return renderTemplate(str, ctx);
      } else if (item instanceof Map<?, ?> map) {
        return renderTemplateMap((Map<String, Object>) map, ctx);
      } else {
        return item;
      }
    }).toList();
  }

  private int calculateFinalPriority(
    WidgetRule rule,
    UserEvent event,
    List<Map<String, Object>> history
  ) {
    int priority = rule.getPriority();

    //  Personalized event → BIG boost
    if (event.getMetadata() != null && !event.getMetadata().isEmpty()) {
      priority += 1000;
    }

    // ️Recent user activity → small boost
    if (history != null) {
      priority += history.size() * 10;
    }

    return priority;
  }


  // =========================================================
  // Simple String Template Engine
  // =========================================================

  private String renderTemplate(String template, Map<String, Object> ctx) {
    if (template == null) return null;

    String result = template;

    for (Map.Entry<String, Object> entry : ctx.entrySet()) {
      String placeholder = "{{" + entry.getKey() + "}}";
      String value = entry.getValue() == null ? "" : entry.getValue().toString();
      result = result.replace(placeholder, value);
    }

    return result;
  }
}
