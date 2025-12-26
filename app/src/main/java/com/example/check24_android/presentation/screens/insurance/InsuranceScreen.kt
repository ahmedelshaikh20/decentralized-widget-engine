package com.example.check24_android.presentation.screens.insurance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.check24_android.presentation.ui.common.PrimaryButton
import com.example.check24_android.presentation.ui.common.WidgetCardWhite
import com.example.check24_android.presentation.ui.theme.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun InsuranceScreen(
  viewModel: InsuranceScreenViewModel = koinViewModel()
) {
  val state by viewModel.state.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    viewModel.effects.collect { effect ->
      when (effect) {
        is InsuranceEffect.ShowMessage -> {
          scope.launch {
            snackbarHostState.showSnackbar(
              message = effect.message,
              duration = SnackbarDuration.Short
            )
          }
        }
      }
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(AppBackground)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      item {
        WidgetCardWhite(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {

            Text(
              text = "Insurance Simulator",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = TextPrimary,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Simulate insurance quote and contract events",
              style = MaterialTheme.typography.bodyMedium,
              color = TextSecondary,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Insurance Info Box
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = Color(0xFFF0FDF4),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "🛡️ Travel Insurance | Coverage from €8",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
              )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Create Quote Button
            PrimaryButton(
              text = "Simulate Insurance Quote",
              enabled = !state.isLoading,
              onClick = {
                viewModel.onEvent(InsuranceScreenEvents.NewQuoteClicked)
              },
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Insurance Button
            PrimaryButton(
              text = "Simulate Insurance Purchase",
              enabled = !state.isLoading,
              onClick = {
                viewModel.onEvent(InsuranceScreenEvents.SignClicked)
              },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }

    // Snackbar
    SnackbarHost(
      hostState = snackbarHostState,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp)
    )
  }
}
