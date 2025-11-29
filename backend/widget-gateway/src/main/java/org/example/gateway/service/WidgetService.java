package org.example.gateway.service;

import org.example.gateway.model.Platform;
import org.example.gateway.model.WidgetDto;
import org.example.gateway.model.WidgetResponse;
import org.example.gateway.repository.WidgetCacheDto;
import org.example.gateway.repository.WidgetCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class WidgetService{

  @Autowired
  private final WidgetCacheRepository repository;

  public WidgetService(WidgetCacheRepository repository) {
    this.repository = repository;
  }


  public WidgetResponse getWidgetsForUser(String userId , Platform platform){
    List<WidgetCacheDto> cachedWidgets = repository.getWidgets(userId);
    if (cachedWidgets == null || cachedWidgets.isEmpty()) {
      return new WidgetResponse(List.of());
    }
    List<WidgetCacheDto> filtered = cachedWidgets.stream()
      .filter(widget -> widget.platformVisibility().contains(platform))
      .toList();

    List<WidgetCacheDto> sorted = filtered.stream()
      .sorted(Comparator.comparingInt(WidgetCacheDto::priority).reversed())
      .toList();

    List<WidgetDto> result = sorted.stream()
      .map(WidgetCacheDto::toBusinessModel)
      .toList();

    return new WidgetResponse(result);
  }
}
