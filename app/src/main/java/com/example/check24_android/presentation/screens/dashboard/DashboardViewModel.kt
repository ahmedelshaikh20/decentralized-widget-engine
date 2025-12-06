package com.example.check24_android.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check24_android.domain.usecases.GetWidgetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DashboardViewModel(val getWidgetsUseCase: GetWidgetsUseCase) : ViewModel() {

  private val _state = MutableStateFlow(DashboardState())
  val state = _state.asStateFlow()


  init {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }

      getWidgetsUseCase()
        .onSuccess { widgetResponse ->
          _state.update {
            it.copy(
              isLoading = false,
              widgets = widgetResponse.widgets
            )
          }
        }
        .onFailure { error ->
          _state.update {
            it.copy(isLoading = false)
          }
          error.printStackTrace() // or show UI error
        }
    }
  }

}
