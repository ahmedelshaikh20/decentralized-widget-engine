package com.example.check24_android.domain.usecases.widgets

import com.example.check24_android.domain.model.toResolved
import com.example.check24_android.domain.repository.WidgetRepository
import com.example.check24_android.presentation.model.ResolvedWidgetResponse

class GetWidgetsUseCase (private val repository: WidgetRepository) {
  suspend operator fun invoke(userId: Int, platform: String): Result<ResolvedWidgetResponse> = repository.getWidgets(userId, platform).map { rawResponse -> rawResponse.toResolved() }
}
