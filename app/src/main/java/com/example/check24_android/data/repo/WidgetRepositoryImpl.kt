package com.example.check24_android.data.repo

import com.example.check24_android.data.api.WidgetApiService
import com.example.check24_android.data.model.toDomain
import com.example.check24_android.domain.model.WidgetResponse
import com.example.check24_android.domain.repository.WidgetRepository

class WidgetRepositoryImpl(private val api: WidgetApiService) : WidgetRepository {

  override suspend fun getWidgets(userId : Int,platform: String): Result<WidgetResponse> {
    return try {
      val response = api.getWidgets(userId,platform)
      Result.success(response.toDomain())
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
