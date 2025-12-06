package com.example.check24_android.domain.model

data class WidgetResponse(
  val widgets: List<Widget>
)

data class Widget(
  val id: String,
  val componentType: String,
  val data: WidgetData
)

data class WidgetData(
  val title: String?,
  val subtitle: String?,
  val imageUrl: String?,
  val iconUrl: String?,
  val items: List<CarouselItem>?,
  val ctaLabel: String?,
  val ctaUrl: String?
)

data class CarouselItem(
  val title: String?,
  val imageUrl: String?,
  val ctaLabel: String?,
  val ctaUrl: String?
)
