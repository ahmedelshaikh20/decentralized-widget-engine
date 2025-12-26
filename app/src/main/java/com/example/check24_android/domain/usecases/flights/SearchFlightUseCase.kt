package com.example.check24_android.domain.usecases.flights

import com.example.check24_android.domain.model.flights.FlightSearchRequest
import com.example.check24_android.domain.repository.FlightRepository

class SearchFlightUseCase(
    private val repository: FlightRepository
) {
    suspend operator fun invoke(search: FlightSearchRequest): Result<Unit> = repository.searchFlight(search)
}
