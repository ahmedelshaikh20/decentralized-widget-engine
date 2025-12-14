package org.example.gateway.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.example.gateway.repository.WidgetCacheRepository;
import org.example.shared.models.widgets.Platform;
import org.example.shared.models.widgets.WidgetCacheDto;
import org.example.shared.models.widgets.WidgetDto;
import org.example.shared.models.widgets.WidgetResponse;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@Service
public class WidgetService {


  private final WidgetCacheRepository repository;
  private final CircuitBreaker circuitBreaker;
  private final FallbackWidgetService fallbackService;
  public WidgetService(WidgetCacheRepository repository, CircuitBreakerRegistry circuitBreakerRegistry , FallbackWidgetService fallbackService) {
    this.repository = repository;
    this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("widgetCache");
    this.fallbackService = fallbackService;
  }


  public void clearPersonalization(String userId) {
    repository.deleteWidgetsForUser(userId);
  }

  public WidgetResponse getWidgetsForUser(String userId, Platform platform) {

    Supplier<List<WidgetCacheDto>> redisCall = () -> repository.getWidgetsMerged(userId);
    Supplier<List<WidgetCacheDto>> protectedCall = CircuitBreaker.decorateSupplier(circuitBreaker, redisCall);

    List<WidgetCacheDto> source;
    try {
      source = protectedCall.get();
      if (source.isEmpty()) {
        source =fallbackService.getCriticalWidgets(platform);
      }
    } catch (Exception ex) {
      source = fallbackService.getCriticalWidgets(platform);
    }

    List<WidgetDto> result = source.stream()
      .filter(widget -> widget.platformVisibility().contains(platform))
      .sorted(Comparator.comparingInt(WidgetCacheDto::priority).reversed())
      .map(WidgetCacheDto::toBusinessModel)
      .toList();

    return new WidgetResponse(result);
  }

}




