package com.example.check24_android.domain.model.insurance

data class InsuranceQuoteRequest(
  val userId: String,
  val insuranceType: InsuranceType
)

enum class InsuranceType {
  TRAVEL,
  CAR,
  HEALTH
}
