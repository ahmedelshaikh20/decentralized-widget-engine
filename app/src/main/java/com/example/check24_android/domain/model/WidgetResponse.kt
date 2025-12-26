package com.example.check24_android.domain.model

import com.example.check24_android.domain.mapper.v1.toBannerV1Payload
import com.example.check24_android.domain.mapper.v1.toCardV1Payload
import com.example.check24_android.domain.mapper.v1.toCarouselV1
import com.example.check24_android.presentation.model.BannerResolvedWidget
import com.example.check24_android.presentation.model.CardResolvedWidget
import com.example.check24_android.presentation.model.CarouselResolvedWidget
import com.example.check24_android.presentation.model.ResolvedWidget
import com.example.check24_android.presentation.model.ResolvedWidgetResponse

data class WidgetResponse(
  val widgets: List<Widget>
)

data class Widget(
  val id: String,
  val componentType: String,
  val payload:WidgetPayload
)


data class WidgetPayload(
  val schemaVersion: String,
  val layout: Map<String, Any?>,
  val content: Map<String, Any?>
)




fun WidgetResponse.toResolved(): ResolvedWidgetResponse =
  ResolvedWidgetResponse(
    widgets = widgets.mapNotNull { it.toResolvedWidget() }
  )

fun Widget.toResolvedWidget(): ResolvedWidget? =
  when (componentType.lowercase()) {

    "carousel" ->
      this.payload.toCarouselV1()?.let {
        CarouselResolvedWidget(
          id = id,
          payload = it
        )
      }


    "card" ->
      this.payload.toCardV1Payload()?.let {
        CardResolvedWidget(
          id = id,
          payload = it
        )
      }


    "banner" ->
      this.payload.toBannerV1Payload()?.let {
        BannerResolvedWidget(
          id = id,
          payload = it
        )
      }


    else -> null
  }







