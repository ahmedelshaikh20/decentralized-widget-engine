package com.example.check24_android.data.model.widgets


import com.example.check24_android.domain.model.Widget
import com.example.check24_android.domain.model.WidgetPayload
import com.example.check24_android.domain.model.WidgetResponse
import com.google.gson.annotations.SerializedName

data class WidgetResponseDto(
  @SerializedName("widgets")
  val widgets: List<WidgetDto> = emptyList()
)

data class WidgetDto(
  @SerializedName("id")
  val id: String,
  @SerializedName("componentType")
  val componentType: String,
  @SerializedName("payload")
  val payload: WidgetPayloadDto
)

data class WidgetPayloadDto(
  @SerializedName("schemaVersion")
  val schemaVersion: String,
  @SerializedName("layout")
  val layout: Map<String, Any?> = emptyMap(),
  @SerializedName("content")
  val content: Map<String, Any?> = emptyMap()
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
    payload = payload.toDomain()
  )

fun WidgetPayloadDto.toDomain(): WidgetPayload =
  WidgetPayload(
    schemaVersion = schemaVersion,
    layout = layout,
    content = content
  )

