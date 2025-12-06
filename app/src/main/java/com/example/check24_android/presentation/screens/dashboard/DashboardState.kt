package com.example.check24_android.presentation.screens.dashboard

import com.example.check24_android.domain.model.Widget

data class DashboardState(
    val isLoading: Boolean = false,
    val widgets: List<Widget> = emptyList(),
)
