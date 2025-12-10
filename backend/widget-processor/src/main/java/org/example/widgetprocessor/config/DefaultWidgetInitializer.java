package org.example.widgetprocessor.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.shared.models.CarouselItem;
import org.example.shared.models.Platform;
import org.example.shared.models.WidgetCacheDto;
import org.example.shared.models.WidgetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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
// Banner Widget - Barcelona Flights
    new WidgetCacheDto(
      "popular-flight-destinations",
      "flight",
      "banner",
      50,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      7200,
      new WidgetData(
        "Fly to Barcelona from €49",
        "Direct flights • Spring deals available now",
        "https://images.unsplash.com/photo-1583422409516-2895a77efded?w=1200&h=600&fit=crop&q=80",
        null,
        null,
        "View Flights",
        "/flight/deals/barcelona"
      )
    ),

// Card Widget: Car Insurance
    new WidgetCacheDto(
      "car-insurance-comparison",
      "insurance",
      "card",
      45,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      86400,
      new WidgetData(
        "Save on Car Insurance",
        "Compare 50+ providers in 2 minutes",
        null,
        "https://api.iconify.design/mdi:shield-car.svg?color=%23007AFF&width=64",
        null,
        "Compare Now",
        "/insurance/car"
      )
    ),

// Card Widget: Internet
    new WidgetCacheDto(
      "internet-tariffs",
      "internet",
      "card",
      40,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      86400,
      new WidgetData(
        "High-Speed Internet from €19.90/mo",
        "Up to 1000 Mbit/s • Available in your area",
        null,
        "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?w=100&h=100&fit=crop&q=80",
        null,
        "Check Availability",
        "/internet/check"
      )
    ),

// Card Widget: Mobile
    new WidgetCacheDto(
      "mobile-plans",
      "mobile",
      "card",
      35,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      86400,
      new WidgetData(
        "Unlimited 5G from €14.99/mo",
        "No contract • Cancel anytime",
        null,
        "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=100&h=100&fit=crop&q=80",
        null,
        "View Plans",
        "/mobile/sim-only"
      )
    ),

// Carousel Widget - Holiday Packages
    new WidgetCacheDto(
      "flight-weekend-trips",
      "travel",
      "carousel",
      30,
      List.of(Platform.WEB, Platform.ANDROID, Platform.IOS),
      7200,
      new WidgetData(
        "Summer 2025 Holiday Packages",
        "All-inclusive resorts • Flights included",
        null,
        null,
        List.of(
          new CarouselItem(
            "Mallorca • 7 nights from €449",
            "https://images.unsplash.com/photo-1583422409516-2895a77efded?w=300&h=200&fit=crop&q=80",
            "View Packages",
            "/holiday/mallorca"
          ),
          new CarouselItem(
            "Antalya • 7 nights from €389",
            "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200?w=300&h=200&fit=crop&q=80",
            "View Packages",
            "/holiday/antalya"
          ),
          new CarouselItem(
            "Crete • 7 nights from €529",
            "https://images.unsplash.com/photo-1613395877344-13d4a8e0d49e?w=300&h=200&fit=crop&q=80",
            "View Packages",
            "/holiday/crete"
          ),
          new CarouselItem(
            "Canary Islands • 7 nights from €499",
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
            "View Packages",
            "/holiday/canary-islands"
          ),
          new CarouselItem(
            "Greek Islands • 7 nights from €549",
            "https://images.unsplash.com/photo-1613395877344-13d4a8e0d49e?w=300&h=200&fit=crop&q=80",
            "View Packages",
            "/holiday/greece"
          )
        ),
        "Browse All Destinations",
        "/holiday/deals"
      )
    ));

  @PostConstruct
  public void loadDefaultsToRedis() throws JsonProcessingException {
    redisTemplate.delete("widgets:defaults"); // clean

    String json = objectMapper.writeValueAsString(defaultWidgets);
    redisTemplate.opsForValue().set("widgets:defaults", json);

    log.info("Stored JSON: {}", json);
  }
}
