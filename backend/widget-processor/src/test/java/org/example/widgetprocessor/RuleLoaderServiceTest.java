package org.example.widgetprocessor;

import org.example.widgetprocessor.model.userevent.EventType;
import org.example.widgetprocessor.model.userevent.UserEvent;
import org.example.widgetprocessor.model.widgetrule.WidgetRule;
import org.example.widgetprocessor.service.ruleservice.RuleEngineService;
import org.example.widgetprocessor.service.ruleservice.RuleLoaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RuleLoaderServiceTest {

    @Autowired
    private RuleLoaderService ruleLoader;
    @Autowired
    private RuleEngineService engine;

    @Test
    void testLoadRules() {
        List<WidgetRule> rules = ruleLoader.getRulesForProduct("test");

        assertNotNull(rules, "Rules list should not be null");
        assertFalse(rules.isEmpty(), "Rules list should not be empty");

        WidgetRule rule = rules.get(0);

        // verify rule fields
        assertEquals("test-card", rule.getId());
        assertEquals("test", rule.getProductId());
        assertEquals("Test Card", rule.getName());
        assertEquals(1, rule.getPriority());
        assertEquals("card", rule.getComponentType());

        // trigger check
        assertEquals(1, rule.getTrigger().getEventTypes().size());
        assertEquals("TEST_EVENT", rule.getTrigger().getEventTypes().get(0));

        // personalization check
        assertEquals("Hello {{name}}", rule.getPersonalization().getTitleTemplate());
        assertEquals("Click", rule.getPersonalization().getCtaLabel());

        System.out.println("Loaded rules: " + rules);
    }

  @Test
  void testMatchingEventType() {
    UserEvent event = new UserEvent("u1", EventType.TEST_EVENT, "test", Map.of("name", "John"),1000);

    List<WidgetRule> rules = engine.calculateEligibleWidgets(event);

    assertEquals(1, rules.size());
    assertEquals("test-card", rules.get(0).getId());
  }

}
