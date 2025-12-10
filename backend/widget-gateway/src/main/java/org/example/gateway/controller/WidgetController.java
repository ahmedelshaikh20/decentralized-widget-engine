package org.example.gateway.controller;


import org.example.gateway.service.WidgetService;
import org.example.shared.models.Platform;
import org.example.shared.models.WidgetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WidgetController {

  public WidgetService widgetService;
  public WidgetController(WidgetService service) {
    this.widgetService = service;
  }

  @GetMapping("/api/v1/widgets")
  public WidgetResponse getWidgets(
    @RequestParam String userId,
    @RequestParam(defaultValue = "WEB") Platform platform
  ) {
    return widgetService.getWidgetsForUser(userId, platform);
  }



  @PostMapping("/api/v1/reset")
  public ResponseEntity<Void> resetWidgets(@RequestParam String userId) {
    widgetService.clearPersonalization(userId);
    return ResponseEntity.noContent().build();
  }


}
