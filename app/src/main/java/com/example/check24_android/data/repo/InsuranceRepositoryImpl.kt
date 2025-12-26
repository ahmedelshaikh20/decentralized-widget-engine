package com.example.check24_android.data.repo

import com.example.check24_android.data.api.InsuranceApi
import com.example.check24_android.data.model.insurance.request.InsurancePurchaseBody
import com.example.check24_android.data.model.insurance.request.InsuranceQuoteBody
import com.example.check24_android.domain.model.insurance.InsurancePurchaseRequest
import com.example.check24_android.domain.model.insurance.InsuranceQuoteRequest
import com.example.check24_android.domain.repository.InsuranceRepository

class InsuranceRepositoryImpl(
  private val api: InsuranceApi
) : InsuranceRepository {

  override suspend fun createQuote(
    request: InsuranceQuoteRequest
  ): Result<Unit> {
    return try {
      api.createQuote(InsuranceQuoteBody(userId = request.userId, type = request.insuranceType.name))
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override suspend fun purchasePolicy(
    request: InsurancePurchaseRequest
  ): Result<Unit> {
    return try {
      api.purchasePolicy(InsurancePurchaseBody(userId = request.userId, quoteId = request.quoteId))
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
