package com.example.check24_android.presentation.di

import com.example.check24_android.presentation.screens.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        DashboardViewModel(
            getWidgetsUseCase = get()
        )
    }
}
