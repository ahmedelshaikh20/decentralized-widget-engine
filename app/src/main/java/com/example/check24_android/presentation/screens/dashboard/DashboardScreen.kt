package com.example.check24_android.presentation.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.check24_android.presentation.common.HomeWidgetRenderer
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
  modifier: Modifier = Modifier,
  viewModel: DashboardViewModel = koinViewModel()
) {
  val state = viewModel.state.collectAsState()

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier.fillMaxSize()
  ) {
    item {
      GreetingHeader()
    }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun GreetingHeader() {
  val hour = java.time.LocalTime.now().hour
  val greeting = when (hour) {
    in 5..11 -> "Guten Morgen 🌅"
    in 12..16 -> "Guten Tag ☀️"
    in 17..21 -> "Guten Abend 🌙"
    else -> "Willkommen 👋"
  }

 Text(
    text = greeting,
    style = MaterialTheme.typography.headlineLarge,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onBackground,
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 8.dp, vertical = 8.dp)
  )
}
