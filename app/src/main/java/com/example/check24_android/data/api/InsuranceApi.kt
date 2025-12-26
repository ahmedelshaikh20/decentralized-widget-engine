package com.example.check24_android.data.api

import com.example.check24_android.data.model.insurance.request.InsurancePurchaseBody
import com.example.check24_android.data.model.insurance.request.InsuranceQuoteBody
import retrofit2.http.Body
import retrofit2.http.POST


interface InsuranceApi {

  @POST("/api/insurance/quote")
  suspend fun createQuote(@Body body: InsuranceQuoteBody)

  @POST("/api/insurance/contract/sign")
  suspend fun purchasePolicy(@Body body: InsurancePurchaseBody)
}
