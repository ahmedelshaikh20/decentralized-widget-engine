package com.example.check24_android.domain.model.flights

import com.example.check24_android.data.model.flights.request.FlightSearchBody

data class FlightSearchRequest(
    val userId: Int,
    val from: String? = null,
    val to: String? = null
)



fun FlightSearchRequest.toData(): FlightSearchBody {
    return FlightSearchBody(
        userId = this.userId,
        from = this.from,
        to = this.to
    )
}
