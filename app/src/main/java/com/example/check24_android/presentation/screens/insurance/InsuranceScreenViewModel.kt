package com.example.check24_android.presentation.screens.insurance

import com.example.check24_android.domain.usecases.insurance.CreateInsuranceQuoteUseCase
import com.example.check24_android.domain.usecases.insurance.PurchaseInsurancePolicyUseCase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check24_android.domain.model.insurance.InsuranceType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InsuranceScreenViewModel(
  private val createInsuranceQuoteUseCase: CreateInsuranceQuoteUseCase,
  private val purchaseInsurancePolicyUseCase: PurchaseInsurancePolicyUseCase
) : ViewModel() {


  private val _state = MutableStateFlow(InsuranceScreenState())
  val state: StateFlow<InsuranceScreenState> = _state.asStateFlow()


  private val _effects = Channel<InsuranceEffect>(Channel.BUFFERED)
  val effects = _effects.receiveAsFlow()


  fun onEvent(event: InsuranceScreenEvents) {
    when (event) {
      InsuranceScreenEvents.NewQuoteClicked -> createQuote()
      InsuranceScreenEvents.SignClicked -> signPolicy()
    }
  }

  private fun createQuote() {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true, error = null) }

      val result = createInsuranceQuoteUseCase(
        userId = "123",
        insuranceType = InsuranceType.TRAVEL
      )

      result.fold(
        onSuccess = {
          _effects.send(
            InsuranceEffect.ShowMessage(
              "Insurance quote created. Personalization will update shortly."
            )
          )
        },
        onFailure = {
          _state.update { state ->
            state.copy(error = "Failed to create quote")
          }
        }
      )

      _state.update { it.copy(isLoading = false) }
    }
  }

  private fun signPolicy() {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true, error = null) }

      val result = purchaseInsurancePolicyUseCase(
        userId = "123",
        quoteId = "quote-123"
      )

      result.fold(
        onSuccess = {
          _effects.send(
            InsuranceEffect.ShowMessage(
              "Insurance signed successfully"
            )
          )
        },
        onFailure = {
          _state.update { state ->
            state.copy(error = "Failed to sign insurance")
          }
        }
      )

      _state.update { it.copy(isLoading = false) }
    }
  }
}
