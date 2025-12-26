package com.example.check24_android.data.api

import com.example.check24_android.data.model.flights.request.FlightBookBody
import com.example.check24_android.data.model.flights.request.FlightSearchBody
import retrofit2.http.Body
import retrofit2.http.POST

interface FlightApiService {


    @POST("api/flight/search")
    suspend fun searchFlight(@Body request: FlightSearchBody)

    @POST("api/flight/book")
    suspend fun bookFlight(@Body request: FlightBookBody)
}
