package com.example.check24_android.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.check24_android.domain.model.WidgetData
import com.example.check24_android.presentation.ui.common.PrimaryButton
import com.example.check24_android.presentation.ui.common.WidgetCardWhite

@Composable
fun CardWidget(
  widget: WidgetData,
  modifier: Modifier = Modifier,
  onCtaClick: (String) -> Unit
) {
  WidgetCardWhite(
    modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top
    ) {
      widget.iconUrl?.let { icon ->
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0F4F8)),
          contentAlignment = Alignment.Center
        ) {
          AsyncImage(
            model = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
          )
        }
        Spacer(Modifier.width(16.dp))
      }

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(vertical = 4.dp)
      ) {
        widget.title?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF1A1A1A)
          )
          Spacer(Modifier.height(6.dp))
        }

        widget.subtitle?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666)
          )
        }
      }
    }

    if (!widget.ctaUrl.isNullOrEmpty() && !widget.ctaLabel.isNullOrEmpty()) {
      Spacer(Modifier.height(16.dp))
      PrimaryButton(
        text = widget.ctaLabel,
        onClick = { onCtaClick(widget.ctaUrl) },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
