package com.example.check24_android.presentation.di

import com.example.check24_android.data.api.FlightApiService
import com.example.check24_android.data.api.InsuranceApi
import com.example.check24_android.data.api.WidgetApiService
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

  single {
    val logging = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }

    OkHttpClient.Builder()
      .addInterceptor(logging)
      .protocols(listOf(Protocol.HTTP_1_1))
      .retryOnConnectionFailure(true)
      .build()

  }

  single {
    Retrofit.Builder()
      .baseUrl("http://165.22.27.127:8083/api/v1/")
      .client(get())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }
  single(named("FLIGHT")) {
    Retrofit.Builder()
      .baseUrl("http://165.22.27.127:9001/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  single<WidgetApiService> { get<Retrofit>().create(WidgetApiService::class.java) }
  single<InsuranceApi> { get<Retrofit>(named("FLIGHT")).create(InsuranceApi::class.java) }
  single<FlightApiService> { get<Retrofit>(named("FLIGHT")).create(FlightApiService::class.java) }
}

