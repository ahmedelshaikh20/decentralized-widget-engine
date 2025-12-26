package com.example.check24_android.domain.repository

import com.example.check24_android.domain.model.insurance.InsurancePurchaseRequest
import com.example.check24_android.domain.model.insurance.InsuranceQuoteRequest

interface InsuranceRepository {
  suspend fun createQuote(request: InsuranceQuoteRequest): Result<Unit>
  suspend fun purchasePolicy(request: InsurancePurchaseRequest): Result<Unit>
}
