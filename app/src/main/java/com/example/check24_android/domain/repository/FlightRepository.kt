package com.example.check24_android.domain.repository

import com.example.check24_android.domain.model.flights.FlightBookingRequest
import com.example.check24_android.domain.model.flights.FlightSearchRequest


interface FlightRepository {
  suspend fun searchFlight(search: FlightSearchRequest): Result<Unit>
  suspend fun bookFlight(booking: FlightBookingRequest): Result<Unit>
}
