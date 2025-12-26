package com.example.check24_android.domain.mapper.v1

import com.example.check24_android.domain.mapper.getMap
import com.example.check24_android.domain.mapper.getString
import com.example.check24_android.domain.mapper.toCta
import com.example.check24_android.domain.model.WidgetPayload
import com.example.check24_android.domain.model.payloads.v1.BannerV1Payload

fun WidgetPayload.toBannerV1Payload(): BannerV1Payload? {
    if (schemaVersion != "v1") return null
    val title = content.getString("title") ?: return null
    return BannerV1Payload(
        title = title,
        subtitle = content.getString("subtitle"),
        imageUrl = content.getString("imageUrl"),
        cta = content.getMap("cta")?.toCta()
    )
}
