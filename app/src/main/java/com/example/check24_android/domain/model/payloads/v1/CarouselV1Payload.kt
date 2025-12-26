package com.example.check24_android.domain.model.payloads.v1

data class CarouselV1Payload(
  val title: String?,
  val subtitle: String?,
  val items: List<CarouselItem>,
  val cta: Cta?
)

data class CarouselItem(
  val title: String,
  val imageUrl: String,
  val ctaLabel: String?,
  val ctaUrl: String?
)

data class Cta(
  val label: String,
  val url: String
)
