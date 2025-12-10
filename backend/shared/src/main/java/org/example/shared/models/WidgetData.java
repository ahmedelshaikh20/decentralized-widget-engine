package org.example.shared.models;


import java.util.List;

public record WidgetData(
  String title,
  String subtitle,
  String imageUrl,
  String iconUrl,
  List<CarouselItem> items,
  String ctaLabel,
  String ctaUrl
) {}
