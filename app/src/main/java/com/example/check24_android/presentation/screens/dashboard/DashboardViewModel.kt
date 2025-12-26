package com.example.check24_android.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check24_android.domain.usecases.widgets.GetWidgetsUseCase
import com.example.check24_android.presentation.usercontext.UserContextRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DashboardViewModel(val getWidgetsUseCase: GetWidgetsUseCase , private val userContextRepo: UserContextRepo) : ViewModel() {

  private val _state = MutableStateFlow(DashboardState())
  val state = _state.asStateFlow()


  init {
    viewModelScope.launch {
      userContextRepo.userFlow.collect { ctx ->
        _state.update { it.copy(userName = ctx.userName, isLoading = true) }
        loadWidgets()
      }
    }
  }


  fun refresh() = loadWidgets()

  private fun loadWidgets() {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }

      val ctx = userContextRepo.getOrInit()
      _state.update { it.copy(userName = ctx.userName) }

      getWidgetsUseCase(ctx.userId, "ANDROID")
        .onSuccess { response ->
          _state.update { it.copy(isLoading = false, widgets = response.widgets) }
        }
        .onFailure {
          _state.update { it.copy(isLoading = false) }
        }
    }
  }

  fun switchUser(userId: Int, userName: String) {
    viewModelScope.launch {
      userContextRepo.switchUser(userId, userName)
    }
  }



}
