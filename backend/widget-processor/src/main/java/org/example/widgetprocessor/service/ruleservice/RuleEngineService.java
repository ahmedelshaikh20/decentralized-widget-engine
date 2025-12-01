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

    public List<WidgetRule> calculateEligibleWidgets(UserEvent event) {
        return ruleLoader.getAllRules().stream()
                .filter(rule -> rule.getTrigger().getEventTypes().contains(event.getEventType()))
                .peek(rule -> log.debug("Matched trigger for rule {}", rule.getId()))
                .collect(Collectors.toList());
    }
}
