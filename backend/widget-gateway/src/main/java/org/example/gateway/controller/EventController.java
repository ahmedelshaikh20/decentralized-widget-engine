package org.example.gateway.controller;

import org.example.gateway.repository.UserEventProducer;
import org.example.shared.models.events.WidgetEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/events")
public class EventController {

  private final UserEventProducer eventProducer;

  public EventController(UserEventProducer eventProducer) {
    this.eventProducer = eventProducer;
  }

  @PostMapping("/widget")
  public ResponseEntity<Void> emitWidgetEvent(
    @RequestBody WidgetEvent event
  ) {
    eventProducer.publish(event);

    return ResponseEntity.accepted().build();
  }
}
