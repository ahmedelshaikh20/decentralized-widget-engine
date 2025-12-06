package com.example.check24_android.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.check24_android.presentation.ui.theme.Check24androidTheme


@Composable
fun WidgetCardGradient(
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp)),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            colors = listOf(
              Color(0xFF005EA8),
              Color(0xFF004680)
            )
          )
        )
        .padding(16.dp),
      content = content
    )
  }
}





@Preview(showBackground = true, name = "Light Mode")
@Composable
fun WidgetCardGradientPreview() {
  Check24androidTheme {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      WidgetCardGradient {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Home,
            contentDescription = null,
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Your Top Flight Deals",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Berlin to Mallorca",
          style = MaterialTheme.typography.bodyMedium,
          color = Color.White.copy(alpha = 0.9f)
        )
        Text(
          text = "€45, Round Trip",
          style = MaterialTheme.typography.titleLarge,
          color = Color.White
        )
      }
    }
  }
}
