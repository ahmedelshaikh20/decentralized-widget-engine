package com.example.check24_android.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.check24_android.domain.model.payloads.v1.BannerV1Payload
import com.example.check24_android.presentation.ui.common.PrimaryButton
import com.example.check24_android.presentation.ui.theme.BrandWhite

@Composable
fun BannerWidget(
  payload: BannerV1Payload,
  modifier: Modifier = Modifier,
  onCtaClick: (String) -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp, vertical = 4.dp)
      .clip(RoundedCornerShape(16.dp))
  ) {
    payload.imageUrl?.let { url ->
      AsyncImage(
        model = url,
        contentDescription = payload.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
      )
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color.Black.copy(alpha = 0.3f),
              Color.Black.copy(alpha = 0.7f)
            ),
            startY = 0f,
            endY = 800f
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
        .padding(20.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Spacer(Modifier.height(8.dp)) // Top spacing

      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        payload.title?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp
            ),
            color = BrandWhite
          )
          Spacer(Modifier.height(8.dp))
        }

        payload.subtitle?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 15.sp
            ),
            color = BrandWhite.copy(alpha = 0.95f)
          )
          Spacer(Modifier.height(16.dp))
        }

        if (payload.cta?.url != null && payload.cta.label.isNotEmpty()) {
          PrimaryButton(
            text = payload.cta.label,
            onClick = { onCtaClick(payload.cta.url) },
            modifier = Modifier.widthIn(max = 200.dp)
          )
        }
      }
    }
  }
}
