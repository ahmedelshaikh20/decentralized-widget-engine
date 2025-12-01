package org.example.widgetprocessor.model.widgetrule;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.widgetprocessor.model.userevent.EventType;

import java.util.List;

public class TriggerConfig {

    private List<EventType> eventTypes;
    private List<String> conditions;

    public TriggerConfig() {}

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}
