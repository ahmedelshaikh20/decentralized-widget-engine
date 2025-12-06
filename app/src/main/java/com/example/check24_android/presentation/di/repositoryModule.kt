package com.example.check24_android.presentation.di

import com.example.check24_android.data.repo.WidgetRepositoryImpl
import com.example.check24_android.domain.repository.WidgetRepository
import com.example.check24_android.domain.usecases.GetWidgetsUseCase
import org.koin.dsl.module

val repositoryModule = module {
    single<WidgetRepository> { WidgetRepositoryImpl(get()) }

    factory {
      GetWidgetsUseCase(get())
    }
}
