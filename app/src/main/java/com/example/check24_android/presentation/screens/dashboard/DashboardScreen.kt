package com.example.check24_android.presentation.screens.dashboard

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.check24_android.presentation.common.HomeWidgetRenderer
import com.example.check24_android.presentation.common.WidgetShimmerPlaceholder
import org.koin.androidx.compose.koinViewModel
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
  modifier: Modifier = Modifier,
  viewModel: DashboardViewModel = koinViewModel()
) {
  val state = viewModel.state.collectAsState()



  PullToRefreshBox(
    isRefreshing = state.value.isLoading,
    onRefresh = { viewModel.refresh() },
    modifier = Modifier.fillMaxSize()
  ){
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier.fillMaxSize()
  ) {
    item {
      GreetingHeader(userName = state.value.userName)
    }
    if (state.value.isLoading && state.value.widgets.isEmpty()) {
      items(4) {
        WidgetShimmerPlaceholder(
          modifier = Modifier.padding(8.dp)
        )
      }
    } else {
    items(state.value.widgets) { widget ->
      HomeWidgetRenderer(
        widget = widget,
        onCtaClick = { url ->
          println("CTA clicked: $url")
        }
      )
    }
    }

  }
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun GreetingHeader(userName: String?) {
  val zone = java.time.ZoneId.systemDefault()
  val now = java.time.ZonedDateTime.now(zone)
  val hour = now.hour
  val baseGreeting = when (hour) {
    in 5..11 -> "Guten Morgen"
    in 12..16 -> "Guten Tag"
    in 17..21 -> "Guten Abend"
    else -> "Willkommen"
  }

  val namePart = userName?.takeIf { it.isNotBlank() }?.let { ", $it" } ?: ""
  val emoji = when (hour) {
    in 5..11 -> " 🌅"
    in 12..16 -> " ☀️"
    in 17..21 -> " 🌙"
    else -> " 👋"
  }

  Text(
    text = "$baseGreeting$namePart$emoji",
    style = MaterialTheme.typography.headlineMedium,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onBackground,
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 8.dp, vertical = 8.dp)
  )
}
