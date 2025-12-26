package com.example.check24_android.domain.model.payloads.v1

data class BannerV1Payload(
    val title: String,
    val subtitle: String?,
    val imageUrl: String?,
    val cta: Cta?
)


