package org.example.widgetprocessor.service.widgetprocessor;

import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.repository.UserHistoryRepository;
import org.example.widgetprocessor.repository.WidgetCacheWriteRepository;
import org.example.widgetprocessor.service.aiservice.DSAiWidgetService;
import org.example.widgetprocessor.service.ruleservice.RuleEngineService;
import org.example.widgetprocessor.service.widgetservice.WidgetBuilderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class UserEventProcessor {

  private final RuleEngineService ruleEngine;
  private final WidgetBuilderService builder;
  private final WidgetCacheWriteRepository widgetRepository;
  private final UserHistoryRepository userHistoryRepository;
  private final DSAiWidgetService aiWidgetService;

  public UserEventProcessor(RuleEngineService ruleEngine, WidgetBuilderService builder, WidgetCacheWriteRepository cache , UserHistoryRepository userHistoryRepository , DSAiWidgetService aiWidgetService) {
    this.ruleEngine = ruleEngine;
    this.builder = builder;
    this.widgetRepository = cache;
    this.userHistoryRepository = userHistoryRepository;
    this.aiWidgetService = aiWidgetService;
  }

  public void processEvent(UserEvent event) {
    String userId = event.getUserId();
    storeEventHistory(event);
    var history = userHistoryRepository.getLastEvents(userId);
    var rules = ruleEngine.calculateEligibleWidgets(event, history);

    if (rules.isEmpty()) return;
    var lastMeta = userHistoryRepository.getLastMetadata(userId, event.getEventType());


    for (var rule : rules) {
      var widget = builder.build(event, rule ,lastMeta , history);

      switch (rule.getAction()) {
        case ADD_WIDGET, UPDATE_WIDGET -> widgetRepository.saveWidget(userId, widget);
        case REMOVE_WIDGET -> widgetRepository.removeWidget(userId, widget.id());
      }
    }
  }


  private void storeEventHistory(UserEvent event) {
    Map<String, Object> entry = new HashMap<>();
    entry.put("eventType", event.getEventType());
    entry.put("productId", event.getProductId());
    entry.put("timestamp", event.getTimestamp());
    entry.put("metadata", event.getMetadata());

    userHistoryRepository.pushEvent(event.getUserId(), entry);

    // Save metadata snapshot for this event type
    userHistoryRepository.updateLastMetadata(
      event.getUserId(),
      event.getEventType().toUpperCase(Locale.ROOT),
      event.getMetadata()
    );
  }


}
