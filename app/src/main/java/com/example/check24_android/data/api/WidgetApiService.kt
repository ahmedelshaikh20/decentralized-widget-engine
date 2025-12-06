package com.example.check24_android.data.api

import com.example.check24_android.data.model.WidgetResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface WidgetApiService {
  @GET("widgets")
  suspend fun getWidgets(@Query("userId") userId: Int ,@Query("platform") platform: String): WidgetResponseDto
}
