package com.example.check24_android.presentation.di

import com.example.check24_android.data.api.WidgetApiService
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
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
            .baseUrl("http://10.0.2.2:8083/api/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<WidgetApiService> {
        get<Retrofit>().create(WidgetApiService::class.java)
    }
}

