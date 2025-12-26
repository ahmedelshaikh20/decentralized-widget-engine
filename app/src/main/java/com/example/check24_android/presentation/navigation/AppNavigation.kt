package com.example.check24_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.check24_android.presentation.screens.dashboard.DashboardScreen
import com.example.check24_android.presentation.screens.flights.FlightScreen
import com.example.check24_android.presentation.screens.insurance.InsuranceScreen

@Composable
fun AppNavigation(navController: NavHostController , modifier: Modifier=Modifier) {
  NavHost(
    modifier = modifier,
  navController = navController,
  startDestination = Dashboard) {
    composable<Dashboard> {
      DashboardScreen()
      }

    composable<Flights> {
      FlightScreen()
    }
    composable<Insurance> {
      InsuranceScreen()
    }
  }
}
