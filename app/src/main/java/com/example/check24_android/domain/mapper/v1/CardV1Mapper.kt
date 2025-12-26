package com.example.check24_android.domain.mapper.v1

import com.example.check24_android.domain.mapper.getMap
import com.example.check24_android.domain.mapper.getString
import com.example.check24_android.domain.mapper.toCta
import com.example.check24_android.domain.model.WidgetPayload
import com.example.check24_android.domain.model.payloads.v1.CardV1Payload

fun WidgetPayload.toCardV1Payload(): CardV1Payload? {
  if (schemaVersion != "v1") return null
  val title = content.getString("title") ?: return null
  return CardV1Payload(
    title = title,
    subtitle = content.getString("subtitle"),
    iconUrl = content.getString("iconUrl"),
    cta = content.getMap("cta")?.toCta()
  )
}
