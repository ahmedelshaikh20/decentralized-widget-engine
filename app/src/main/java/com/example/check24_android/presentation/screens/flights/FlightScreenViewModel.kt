package com.example.check24_android.presentation.screens.flights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check24_android.domain.model.flights.FlightBookingRequest
import com.example.check24_android.domain.model.flights.FlightSearchRequest
import com.example.check24_android.domain.usecases.flights.BookFlightUseCase
import com.example.check24_android.domain.usecases.flights.SearchFlightUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightScreenViewModel(
  private val searchFlight: SearchFlightUseCase,
  private val bookFlight: BookFlightUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FlightState())
    val state: StateFlow<FlightState> = _state.asStateFlow()

    private val _effect = Channel<FlightEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: FlightScreenEvents) {
        when (event) {
          FlightScreenEvents.SearchFlightClicked -> search()
          FlightScreenEvents.BookFlightClicked -> book()
        }
    }


    private fun search() = launchAction(
        action = FlightAction.SEARCH_SENT
    ) {
        searchFlight(FlightSearchRequest(userId = 123))
    }

    private fun book() = launchAction(
        action = FlightAction.BOOKING_SENT
    ) {
        bookFlight(FlightBookingRequest(userId = 123, to = "BCN"))
    }

    private fun launchAction(
        action: FlightAction,
        block: suspend () -> Result<Unit>
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            block()
                .onSuccess {
                    _state.update {
                        it.copy(isLoading = false, lastAction = action)
                    }
                    _effect.send(FlightEffect.ShowMessage("Action sent successfully"))
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                    _effect.send(FlightEffect.ShowMessage(error.message ?: "Something went wrong"))
                }
        }
    }
}
