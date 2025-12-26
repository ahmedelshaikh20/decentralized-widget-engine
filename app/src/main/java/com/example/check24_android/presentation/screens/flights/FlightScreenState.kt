package com.example.check24_android.presentation.screens.flights

data class FlightState(
    val isLoading: Boolean = false,
    val lastAction: FlightAction? = null,
    val error: String? = null
)

enum class FlightAction {
    SEARCH_SENT,
    BOOKING_SENT
}
