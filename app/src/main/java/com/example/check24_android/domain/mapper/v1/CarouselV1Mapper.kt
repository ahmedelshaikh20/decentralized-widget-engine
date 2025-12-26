package com.example.check24_android.domain.mapper.v1

import com.example.check24_android.domain.model.WidgetPayload
import com.example.check24_android.domain.model.payloads.v1.CarouselItem
import com.example.check24_android.domain.model.payloads.v1.CarouselV1Payload
import com.example.check24_android.domain.model.payloads.v1.Cta

fun WidgetPayload.toCarouselV1(): CarouselV1Payload? {
    if (schemaVersion != "v1") return null
    val itemsRaw = content["items"] as? List<Map<String, Any>> ?: return null
    val items = itemsRaw.mapNotNull { item ->
        val title = item["title"] as? String ?: return@mapNotNull null
        val imageUrl = item["imageUrl"] as? String ?: return@mapNotNull null
        CarouselItem(
            title = title,
            imageUrl = imageUrl,
            ctaLabel = item["ctaLabel"] as? String,
            ctaUrl = item["ctaUrl"] as? String
        )
    }
    val cta = (content["cta"] as? Map<*, *>)?.let {
        val label = it["label"] as? String
        val url = it["url"] as? String
        if (label != null && url != null) Cta(label, url) else null
    }

    return CarouselV1Payload(
        title = content["title"] as? String,
        subtitle = content["subtitle"] as? String,
        items = items,
        cta = cta
    )
}
