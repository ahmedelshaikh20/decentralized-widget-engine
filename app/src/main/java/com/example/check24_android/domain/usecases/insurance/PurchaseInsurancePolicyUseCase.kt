package com.example.check24_android.domain.usecases.insurance

import com.example.check24_android.domain.model.insurance.InsurancePurchaseRequest
import com.example.check24_android.domain.repository.InsuranceRepository

class PurchaseInsurancePolicyUseCase(
  private val repository: InsuranceRepository
) {
  suspend operator fun invoke(userId: String, quoteId: String): Result<Unit> = repository.purchasePolicy(InsurancePurchaseRequest(userId = userId, quoteId = quoteId))
}
