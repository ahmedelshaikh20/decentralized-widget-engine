package com.example.check24_android.data.model.flights.request

data class FlightSearchBody(
    val userId: Int,
    val from: String? = null,
    val to: String? = null
)
