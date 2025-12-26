package com.example.check24_android.presentation.di

import com.example.check24_android.data.repo.FlightRepositoryImpl
import com.example.check24_android.data.repo.InsuranceRepositoryImpl
import com.example.check24_android.data.repo.WidgetRepositoryImpl
import com.example.check24_android.domain.repository.FlightRepository
import com.example.check24_android.domain.repository.InsuranceRepository
import com.example.check24_android.domain.repository.WidgetRepository
import com.example.check24_android.domain.usecases.widgets.GetWidgetsUseCase
import com.example.check24_android.presentation.usercontext.UserContextRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<WidgetRepository> { WidgetRepositoryImpl(get()) }
    single<FlightRepository> { FlightRepositoryImpl(get()) }
    single<InsuranceRepository> { InsuranceRepositoryImpl(get()) }
    factory {
      GetWidgetsUseCase(get())
    }


  // UserContext Repo
  single { UserContextRepo(androidContext()) }
}
