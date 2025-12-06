package com.example.check24_android.data.model


import com.example.check24_android.domain.model.CarouselItem
import com.example.check24_android.domain.model.Widget
import com.example.check24_android.domain.model.WidgetData
import com.example.check24_android.domain.model.WidgetResponse
import com.google.gson.annotations.SerializedName

data class WidgetResponseDto(
    @SerializedName("widgets")
    val widgets: List<WidgetDto>
)

data class WidgetDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("componentType")
    val componentType: String,
    @SerializedName("data")
    val data: WidgetDataDto
)

data class WidgetDataDto(
  @SerializedName("title") val title: String?,
  @SerializedName("subtitle") val subtitle: String?,
  @SerializedName("imageUrl") val imageUrl: String?,
  @SerializedName("iconUrl") val iconUrl: String?,
  @SerializedName("items") val items: List<CarouselItemDto>?,
  @SerializedName("ctaLabel") val ctaText: String?,
  @SerializedName("ctaUrl") val ctaUrl: String?
)

data class CarouselItemDto(
  @SerializedName("title") val title: String?,
  @SerializedName("imageUrl") val imageUrl: String?,
  @SerializedName("ctaLabel") val ctaLabel: String?,
  @SerializedName("ctaUrl") val ctaUrl: String?
)



// Mapper functions to domain layer

fun WidgetResponseDto.toDomain(): WidgetResponse =
  WidgetResponse(
    widgets = widgets.map { it.toDomain() }
  )

fun WidgetDto.toDomain(): Widget =
  Widget(
    id = id,
    componentType = componentType,
    data = data.toDomain()
  )

fun WidgetDataDto.toDomain(): WidgetData =
  WidgetData(
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    iconUrl = iconUrl,
    items = items?.map { it.toDomain() },
    ctaLabel = ctaText,
    ctaUrl = ctaUrl
  )

fun CarouselItemDto.toDomain(): CarouselItem =
  CarouselItem(
    title = title,
    imageUrl = imageUrl,
    ctaLabel = ctaLabel,
    ctaUrl = ctaUrl
  )
