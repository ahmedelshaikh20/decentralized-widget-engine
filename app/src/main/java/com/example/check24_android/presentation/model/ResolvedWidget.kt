package com.example.check24_android.presentation.model

import com.example.check24_android.domain.model.payloads.v1.BannerV1Payload
import com.example.check24_android.domain.model.payloads.v1.CardV1Payload
import com.example.check24_android.domain.model.payloads.v1.CarouselV1Payload

sealed interface ResolvedWidget {
    val id: String
}


data class CarouselResolvedWidget(
  override val id: String,
  val payload: CarouselV1Payload
) : ResolvedWidget

data class CardResolvedWidget(
  override val id: String,
  val payload: CardV1Payload
) : ResolvedWidget

data class BannerResolvedWidget(
  override val id: String,
  val payload: BannerV1Payload
) : ResolvedWidget
