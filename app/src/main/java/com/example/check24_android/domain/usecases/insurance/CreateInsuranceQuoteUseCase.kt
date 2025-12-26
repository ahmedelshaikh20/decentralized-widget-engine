package com.example.check24_android.domain.usecases.insurance

import com.example.check24_android.domain.model.insurance.InsuranceQuoteRequest
import com.example.check24_android.domain.model.insurance.InsuranceType
import com.example.check24_android.domain.repository.InsuranceRepository

class CreateInsuranceQuoteUseCase(
  private val repository: InsuranceRepository
) {

  suspend operator fun invoke(userId: String, insuranceType: InsuranceType): Result<Unit> = repository.createQuote(InsuranceQuoteRequest(userId = userId, insuranceType = insuranceType))

}
