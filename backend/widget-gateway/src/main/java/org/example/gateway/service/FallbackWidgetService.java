package org.example.gateway.service;

import org.example.shared.models.widgets.Platform;
import org.example.shared.models.widgets.WidgetCacheDto;
import org.example.shared.models.widgets.WidgetPayload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FallbackWidgetService {

  public List<WidgetCacheDto> getCriticalWidgets(Platform platform) {
    return getCriticalDefaults().stream()
      .filter(w -> w.platformVisibility().contains(platform))
      .collect(Collectors.toList());
  }

  private List<WidgetCacheDto> getCriticalDefaults() {

    return List.of(

      // ---------------- Banner Widget ----------------
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

      // ---------------- Card Widget: Car Insurance ----------------
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

      // ---------------- Card Widget: Internet ----------------
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

      // ---------------- Card Widget: Mobile ----------------
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

      // ---------------- Carousel Widget: Holiday Packages ----------------
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
              )
            ),
            "cta", Map.of(
              "label", "Browse All Destinations",
              "url", "/holiday/deals"
            )
          )
        )
      )
    );
  }
}
