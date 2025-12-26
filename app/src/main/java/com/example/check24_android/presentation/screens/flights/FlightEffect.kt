package com.example.check24_android.presentation.screens.flights

sealed interface FlightEffect {
    data class ShowMessage(val message: String) : FlightEffect
}
