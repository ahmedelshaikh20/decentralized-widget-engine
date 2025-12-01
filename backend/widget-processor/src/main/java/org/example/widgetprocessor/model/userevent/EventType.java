package org.example.widgetprocessor.model.userevent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum EventType {

  FLIGHT_SEARCH,
  FLIGHT_BOOKED,
  INSURANCE_QUOTE,
  INSURANCE_COMPLETED,
  FURNITURE_BROWSE,
  FURNITURE_FAVORITE,
  CONTRACT_SIGNED,
  HOME_VISIT,

  TEST_EVENT;

  private static final Logger log = LoggerFactory.getLogger(EventType.class);


  public static EventType fromString(String raw) {
    try {
      return EventType.valueOf(raw.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      log.warn("Unknown event type '{}', defaulting to TEST_EVENT", raw);
      return TEST_EVENT;
    }
  }
}

