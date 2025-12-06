package com.example.check24_android.domain.repository

import com.example.check24_android.domain.model.WidgetResponse


interface WidgetRepository {

    suspend fun getWidgets(userId : Int,platform: String): Result<WidgetResponse>
}
