package org.example.widgetprocessor.model.widgetrule;

import java.util.List;

public class TriggerConfig {

    private List<String> eventTypes;
    private List<String> conditions;

    public TriggerConfig() {}

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}
