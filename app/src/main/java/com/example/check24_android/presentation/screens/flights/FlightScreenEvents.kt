package com.example.check24_android.presentation.screens.flights

sealed class FlightScreenEvents {

    data object SearchFlightClicked : FlightScreenEvents()
    data object BookFlightClicked : FlightScreenEvents()
}
