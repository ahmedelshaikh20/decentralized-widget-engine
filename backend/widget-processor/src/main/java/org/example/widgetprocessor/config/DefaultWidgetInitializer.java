package org.example.widgetprocessor.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.shared.models.widgets.Platform;
import org.example.shared.models.widgets.WidgetCacheDto;
import org.example.shared.models.widgets.WidgetPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultWidgetInitializer {

  private static final Logger log = LoggerFactory.getLogger(DefaultWidgetInitializer.class);
  private final ObjectMapper objectMapper;
  private final StringRedisTemplate redisTemplate;

  public DefaultWidgetInitializer(StringRedisTemplate redis , @Qualifier("jsonObjectMapper") ObjectMapper objectMapper) {
    this.redisTemplate = redis;
    this.objectMapper = objectMapper;
  }

  private final List<WidgetCacheDto> defaultWidgets = List.of(

    new WidgetCacheDto(
      "popular-flight-destinations",
      "flight",
      "banner",
      50,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      7200,
      new WidgetPayload(
        "v1",
        Map.of(), // layout
        Map.of(
          "title", "Fly to Barcelona from €49",
          "subtitle", "Direct flights • Spring deals available now",
          "imageUrl",
          "https://images.unsplash.com/photo-1583422409516-2895a77efded?w=1200&h=600&fit=crop&q=80",
          "cta", Map.of(
            "label", "View Flights",
            "url", "/flight/deals/barcelona"
          )
        )
      )
    ),

    new WidgetCacheDto(
      "car-insurance-comparison",
      "insurance",
      "card",
      45,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      86400,
      new WidgetPayload(
        "v1",
        Map.of(
          "style", "standard"
        ),
        Map.of(
          "title", "Save on Car Insurance",
          "subtitle", "Compare 50+ providers in 2 minutes",
          "iconUrl",
          "https://api.iconify.design/mdi:shield-car.svg?color=%23007AFF&width=64",
          "cta", Map.of(
            "label", "Compare Now",
            "url", "/insurance/car"
          )
        )
      )
    ),

    new WidgetCacheDto(
      "internet-tariffs",
      "internet",
      "card",
      40,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      86400,
      new WidgetPayload(
        "v1",
        Map.of(),
        Map.of(
          "title", "High-Speed Internet from €19.90/mo",
          "subtitle", "Up to 1000 Mbit/s • Available in your area",
          "iconUrl",
          "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?w=100&h=100&fit=crop&q=80",
          "cta", Map.of(
            "label", "Check Availability",
            "url", "/internet/check"
          )
        )
      )
    ),

    new WidgetCacheDto(
      "mobile-plans",
      "mobile",
      "card",
      35,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      86400,
      new WidgetPayload(
        "v1",
        Map.of(),
        Map.of(
          "title", "Unlimited 5G from €14.99/mo",
          "subtitle", "No contract • Cancel anytime",
          "iconUrl",
          "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=100&h=100&fit=crop&q=80",
          "cta", Map.of(
            "label", "View Plans",
            "url", "/mobile/sim-only"
          )
        )
      )
    ),

    new WidgetCacheDto(
      "flight-weekend-trips",
      "travel",
      "carousel",
      30,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      7200,
      new WidgetPayload(
        "v1",
        Map.of(
          "itemsPerViewport", 1.2
        ),
        Map.of(
          "title", "Summer 2025 Holiday Packages",
          "subtitle", "All-inclusive resorts • Flights included",
          "items", List.of(
            Map.of(
              "title", "Mallorca • 7 nights from €449",
              "imageUrl",
              "https://images.unsplash.com/photo-1583422409516-2895a77efded?w=300&h=200&fit=crop&q=80",
              "ctaLabel", "View Packages",
              "ctaUrl", "/holiday/mallorca"
            ),
            Map.of(
              "title", "Antalya • 7 nights from €389",
              "imageUrl",
              "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200?w=300&h=200&fit=crop&q=80",
              "ctaLabel", "View Packages",
              "ctaUrl", "/holiday/antalya"
            ),
            Map.of(
              "title", "Crete • 7 nights from €529",
              "imageUrl",
              "https://images.unsplash.com/photo-1613395877344-13d4a8e0d49e?w=300&h=200&fit=crop&q=80",
              "ctaLabel", "View Packages",
              "ctaUrl", "/holiday/crete"
            ),
            Map.of(
              "title", "Canary Islands • 7 nights from €499",
              "imageUrl",
              "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
              "ctaLabel", "View Packages",
              "ctaUrl", "/holiday/canary-islands"
            ),
            Map.of(
              "title", "Greek Islands • 7 nights from €549",
              "imageUrl",
              "https://images.unsplash.com/photo-1613395877344-13d4a8e0d49e?w=300&h=200&fit=crop&q=80",
              "ctaLabel", "View Packages",
              "ctaUrl", "/holiday/greece"
            )
          ),
          "cta", Map.of(
            "label", "Browse All Destinations",
            "url", "/holiday/deals"
          )
        )
      )));
  

  @PostConstruct
  public void loadDefaultsToRedis() throws JsonProcessingException {
    redisTemplate.delete("widgets:defaults");

    String json = objectMapper.writeValueAsString(defaultWidgets);
    redisTemplate.opsForValue().set("widgets:defaults", json);

    log.info("Stored JSON: {}", json);
  }
}
