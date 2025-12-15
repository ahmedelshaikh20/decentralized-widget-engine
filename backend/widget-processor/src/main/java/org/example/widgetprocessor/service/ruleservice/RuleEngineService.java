package org.example.widgetprocessor.service.ruleservice;

import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleEngineService {

  private static final Logger log = LoggerFactory.getLogger(RuleEngineService.class);

  private final RuleLoaderService ruleLoader;

  public RuleEngineService(RuleLoaderService ruleLoader) {
    this.ruleLoader = ruleLoader;
  }

  /**
   * Main rule evaluation entrypoint.
   */
  public List<WidgetRule> calculateEligibleWidgets(UserEvent event) {
    return ruleLoader.getAllRules().stream()
      .filter(rule -> matchesEventType(rule, event))
      .collect(Collectors.toList());
  }

  /**
   * Rule only triggers if eventType is part of rule.trigger.eventTypes.
   */
  private boolean matchesEventType(WidgetRule rule, UserEvent event) {
    List<String> eventTypes = rule.getTrigger().getEventTypes();
    if (eventTypes == null || eventTypes.isEmpty()) {
      log.warn("Rule {} has no eventTypes defined → skipping", rule.getId());
      return false;
    }

    boolean match = eventTypes.contains(event.getEventType());
    log.debug("Rule {} eventType match: {}", rule.getId(), match);
    return match;
  }








}
