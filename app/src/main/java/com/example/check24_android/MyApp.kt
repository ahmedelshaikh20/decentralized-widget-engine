package com.example.check24_android

import android.app.Application
import com.example.check24_android.presentation.di.networkModule
import com.example.check24_android.presentation.di.repositoryModule
import com.example.check24_android.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@MyApp)
      modules(
        networkModule,
        repositoryModule,
        viewModelModule
      )
    }
  }
}
