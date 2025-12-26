package com.example.check24_android.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.check24_android.domain.model.payloads.v1.CarouselV1Payload
import com.example.check24_android.presentation.ui.common.PrimaryButton
import com.example.check24_android.presentation.ui.common.WidgetCardGradient
import com.example.check24_android.presentation.ui.theme.BrandWhite

@Composable
fun CarouselWidget(
  payload: CarouselV1Payload,
  modifier: Modifier = Modifier,
  onCtaClick: (String) -> Unit,
  onItemClick: (String) -> Unit = {}
) {


  WidgetCardGradient(
    modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
  ) {
    payload.title?.let {
      Text(
        text = it,
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp
        ),
        color = BrandWhite
      )
      Spacer(Modifier.height(6.dp))
    }

    payload.subtitle?.let {
      Text(
        text = it,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontSize = 15.sp
        ),
        color = BrandWhite.copy(alpha = 0.9f)
      )
      Spacer(Modifier.height(16.dp))
    }

    payload.items.let { carouselItems ->
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(carouselItems) { item ->
          CarouselCard(
            title = item.title,
            imageUrl = item.imageUrl,
            ctaLabel = item.ctaLabel,
            onClick = { item.ctaUrl?.let { onItemClick(it) } }
          )
        }
      }
      Spacer(Modifier.height(16.dp))
    }

    if (payload.cta != null && payload.cta.label.isNotEmpty()) {
      PrimaryButton(
        text = payload.cta.label,
        onClick = { onCtaClick(payload.cta.url) },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
private fun CarouselCard(
  title: String?,
  imageUrl: String?,
  ctaLabel: String?,
  onClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .width(200.dp)
      .height(160.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() },
    shadowElevation = 2.dp,
    shape = RoundedCornerShape(16.dp)
  ) {
    Box(Modifier.fillMaxSize()) {

      AsyncImage(
        model = imageUrl,
        contentDescription = title,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
              startY = 100f
            )
          )
      )

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
      ) {
        Text(
          text = title.orEmpty(),
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
          ),
          color = BrandWhite,
          maxLines = 2
        )

        ctaLabel?.let {
          Spacer(Modifier.height(4.dp))
          Text(
            text = it,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
            color = BrandWhite.copy(alpha = 0.9f)
          )
        }
      }
    }
  }
}
