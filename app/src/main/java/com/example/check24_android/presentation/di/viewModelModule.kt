package com.example.check24_android.presentation.di

import com.example.check24_android.data.repo.FlightRepositoryImpl
import com.example.check24_android.domain.repository.FlightRepository
import com.example.check24_android.domain.usecases.flights.BookFlightUseCase
import com.example.check24_android.domain.usecases.flights.SearchFlightUseCase
import com.example.check24_android.domain.usecases.insurance.CreateInsuranceQuoteUseCase
import com.example.check24_android.domain.usecases.insurance.PurchaseInsurancePolicyUseCase
import com.example.check24_android.presentation.screens.dashboard.DashboardViewModel
import com.example.check24_android.presentation.screens.flights.FlightScreenViewModel
import com.example.check24_android.presentation.screens.insurance.InsuranceScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  factory { SearchFlightUseCase(get()) }
  factory { BookFlightUseCase(get()) }
  factory { CreateInsuranceQuoteUseCase(get()) }
  factory { PurchaseInsurancePolicyUseCase(get()) }
  viewModel { DashboardViewModel(get(), get()) }
  viewModel { FlightScreenViewModel(searchFlight = get(), bookFlight = get()) }
  viewModel { InsuranceScreenViewModel(createInsuranceQuoteUseCase = get(), purchaseInsurancePolicyUseCase = get()) }
}
