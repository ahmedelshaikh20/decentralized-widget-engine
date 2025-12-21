package org.example.widgetprocessor.service.ruleservice;

import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
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

    List<WidgetRule> matched = ruleLoader.getAllRules().stream()
      .filter(rule -> rule.getProductId().equals(event.getProductId()))
      .filter(rule -> matchesEventType(rule, event))
      .sorted((a, b) -> Integer.compare(b.getPriority(), a.getPriority()))
      .collect(Collectors.toList());

    return matched;
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
