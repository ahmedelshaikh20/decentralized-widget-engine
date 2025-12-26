package com.example.check24_android.domain.mapper

import com.example.check24_android.domain.model.payloads.v1.Cta

fun Map<String, Any?>.getString(key: String): String? =
    this[key] as? String

fun Map<String, Any?>.getMap(key: String): Map<String, Any?>? =
    this[key] as? Map<String, Any?>


fun Map<String, Any?>.toCta(): Cta? {
  val label = getString("label")
  val url = getString("url")

  return if (label != null && url != null) {
    Cta(label, url)
  } else {
    null
  }
}
