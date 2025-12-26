package com.example.check24_android.presentation.screens.dashboard

import com.example.check24_android.presentation.model.ResolvedWidget

data class DashboardState(
    val isLoading: Boolean = false,
    val widgets: List<ResolvedWidget> = emptyList(),
    val userId: Int = 123,
    val userName: String? = null
)
