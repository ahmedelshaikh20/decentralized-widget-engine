package com.example.check24_android.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.check24_android.presentation.ui.theme.Check24androidTheme

@Composable
fun WidgetShimmerPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(shimmerBrush())
    )
}


@Composable
fun shimmerBrush(): Brush {
  val transition = rememberInfiniteTransition(label = "")
  val translateAnim = transition.animateFloat(
    initialValue = 0f,
    targetValue = 1000f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1200, easing = LinearEasing)
    ),
    label = ""
  )

  return Brush.linearGradient(
    colors = listOf(
      Color.LightGray.copy(alpha = 0.3f),
      Color.LightGray.copy(alpha = 0.1f),
      Color.LightGray.copy(alpha = 0.3f),
    ),
    start = Offset(translateAnim.value, translateAnim.value),
    end = Offset(translateAnim.value + 200f, translateAnim.value + 200f)
  )
}



@Preview(showBackground = true)
@Composable
fun ShimmerPreview() {
  Check24androidTheme {
    WidgetShimmerPlaceholder(
      modifier = Modifier.padding(16.dp)
    )
  }
}
