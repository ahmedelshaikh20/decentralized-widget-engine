package com.example.check24_android.domain.usecases

import com.example.check24_android.domain.repository.WidgetRepository

class GetWidgetsUseCase (private val repository: WidgetRepository) {
    suspend operator fun invoke() = repository.getWidgets(123,"ANDROID")
}
