package org.example.gateway.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.example.gateway.repository.WidgetCacheRepository;
import org.example.shared.models.Platform;
import org.example.shared.models.WidgetCacheDto;
import org.example.shared.models.WidgetDto;
import org.example.shared.models.WidgetResponse;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@Service
public class WidgetService {


  private final WidgetCacheRepository repository;
  private final CircuitBreaker circuitBreaker;
  public WidgetService(WidgetCacheRepository repository, CircuitBreakerRegistry circuitBreakerRegistry) {
    this.repository = repository;
    this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("widgetCache");
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
    } catch (Exception ex) {
      source = List.of();
    }

    if (source.isEmpty()) {
      return new WidgetResponse(List.of());
    }
    List<WidgetDto> result = source.stream()
      .filter(widget -> widget.platformVisibility().contains(platform))
      .sorted(Comparator.comparingInt(WidgetCacheDto::priority).reversed())
      .map(WidgetCacheDto::toBusinessModel)
      .toList();

    return new WidgetResponse(result);
  }

}
