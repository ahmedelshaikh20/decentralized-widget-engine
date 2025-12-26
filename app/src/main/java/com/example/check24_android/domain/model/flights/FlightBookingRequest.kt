package com.example.check24_android.domain.model.flights

import com.example.check24_android.data.model.flights.request.FlightBookBody

data class FlightBookingRequest(
    val userId: Int,
    val to: String? = null
)




fun FlightBookingRequest.toData(): FlightBookBody {
    return FlightBookBody(
        userId = this.userId,
        to = this.to
    )
}
