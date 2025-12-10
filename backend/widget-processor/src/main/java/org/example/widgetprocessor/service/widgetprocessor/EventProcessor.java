package org.example.widgetprocessor.service.widgetprocessor;

import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.repository.WidgetCacheWriteRepository;
import org.example.widgetprocessor.service.ruleservice.RuleEngineService;
import org.example.widgetprocessor.service.widgetservice.WidgetBuilderService;
import org.springframework.stereotype.Service;

@Service
public class EventProcessor {

  private final RuleEngineService ruleEngine;
  private final WidgetBuilderService builder;
  private final WidgetCacheWriteRepository cache;

  public EventProcessor(RuleEngineService ruleEngine, WidgetBuilderService builder, WidgetCacheWriteRepository cache) {
    this.ruleEngine = ruleEngine;
    this.builder = builder;
    this.cache = cache;
  }

  public void processEvent(UserEvent event) {

    var rules = ruleEngine.calculateEligibleWidgets(event);
    if (rules.isEmpty()) return;

    String userId = event.getUserId();

    for (var rule : rules) {
      var widget = builder.build(event, rule);

      switch (rule.getAction()) {
        case ADD_WIDGET, UPDATE_WIDGET -> cache.saveWidget(userId, widget);
        case REMOVE_WIDGET -> cache.removeWidget(userId, widget.id());
      }
    }
  }
}
