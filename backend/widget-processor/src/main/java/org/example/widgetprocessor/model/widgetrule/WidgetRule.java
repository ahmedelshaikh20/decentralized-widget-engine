package org.example.widgetprocessor.model.widgetrule;


import org.example.shared.models.Platform;

import java.util.List;
import java.util.Map;

public class WidgetRule {

    private String id;
    private String name;

    private String productId;   // set by RuleLoader (file name)

    private TriggerConfig trigger;
    private PersonalizationConfig personalization;

    private String componentType;
    private int priority;
    private List<Platform> platformVisibility;
    private int ttlSeconds;

    private Map<String, Object> layoutConfig;

    private RuleAction action = RuleAction.ADD_WIDGET;


    public WidgetRule() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public TriggerConfig getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerConfig trigger) {
        this.trigger = trigger;
    }

    public PersonalizationConfig getPersonalization() {
        return personalization;
    }

    public void setPersonalization(PersonalizationConfig personalization) {
        this.personalization = personalization;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Platform> getPlatformVisibility() {
        return platformVisibility;
    }

    public void setPlatformVisibility(List<Platform> platformVisibility) {
        this.platformVisibility = platformVisibility;
    }

    public int getTtlSeconds() {
        return ttlSeconds;
    }

    public void setTtlSeconds(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    public Map<String, Object> getLayoutConfig() {
        return layoutConfig;
    }

    public void setLayoutConfig(Map<String, Object> layoutConfig) {
        this.layoutConfig = layoutConfig;
    }

    public RuleAction getAction() {
        return action;
    }

    public void setAction(RuleAction action) {
        this.action = action;
    }
}
