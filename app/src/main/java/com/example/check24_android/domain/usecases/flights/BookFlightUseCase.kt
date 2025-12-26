package com.example.check24_android.domain.usecases.flights

import com.example.check24_android.domain.model.flights.FlightBookingRequest
import com.example.check24_android.domain.repository.FlightRepository

class BookFlightUseCase(
    private val repository: FlightRepository
) {
    suspend operator fun invoke(booking: FlightBookingRequest): Result<Unit> = repository.bookFlight(booking)
}
