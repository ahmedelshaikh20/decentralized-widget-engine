package com.example.check24_android.data.repo

import com.example.check24_android.data.api.FlightApiService
import com.example.check24_android.domain.model.flights.FlightBookingRequest
import com.example.check24_android.domain.model.flights.FlightSearchRequest
import com.example.check24_android.domain.model.flights.toData
import com.example.check24_android.domain.repository.FlightRepository

class FlightRepositoryImpl(
  private val api: FlightApiService
) : FlightRepository {
  override suspend fun searchFlight(search: FlightSearchRequest): Result<Unit> =
    runCatching { api.searchFlight(search.toData()) }

  override suspend fun bookFlight(booking: FlightBookingRequest): Result<Unit> =
    runCatching { api.bookFlight(booking.toData()) }
}

