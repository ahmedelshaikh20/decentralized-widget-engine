package com.example.check24_android.presentation.ui.common
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.check24_android.presentation.ui.theme.AppBackground
import com.example.check24_android.presentation.ui.theme.AppCardDefaults
import com.example.check24_android.presentation.ui.theme.BrandWhite
import com.example.check24_android.presentation.ui.theme.DividerColor
import com.example.check24_android.presentation.ui.theme.StatusActive
import com.example.check24_android.presentation.ui.theme.TextPrimary
import com.example.check24_android.presentation.ui.theme.TextSecondary


@Composable
fun WidgetCardWhite(
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCardDefaults.cornerRadius),
        elevation = CardDefaults.cardElevation(AppCardDefaults.elevation),
        colors = CardDefaults.cardColors(containerColor = BrandWhite)
    ) {
        Column(
            modifier = Modifier.padding(AppCardDefaults.padding),
            content = content
        )
    }
}



@Preview(showBackground = true, name = "Insurance Card Context")
@Composable
fun WidgetCardWhitePreview() {
  // 1. Use the new "Cool Gray" background so the white card pops
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(AppBackground)
      .padding(16.dp)
  ) {
    WidgetCardWhite {
      // -- Header --
      Text(
        text = "Active Contracts Summary",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TextPrimary // Swapped from Color.Black
      )

      Spacer(modifier = Modifier.height(12.dp))

      // -- Row 1: Car Insurance --
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Car Insurance",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary // Swapped from Color.DarkGray
        )
        Text(
          text = "Active, ends 12/25",
          color = StatusActive, // <--- FIXED: The missing color
          style = MaterialTheme.typography.bodyMedium
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // -- Divider --
      // Use the theme divider color instead of hardcoded Gray
      Divider(color = DividerColor, thickness = 1.dp)

      Spacer(modifier = Modifier.height(8.dp))

      // -- Row 2: Liability --
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Liability Insurance",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )
        Text(
          text = "Active, ends 03/26",
          color = StatusActive, // Consistent Green
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}


