package com.example.check24_android.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography


private val LightColorScheme = lightColorScheme(
  primary = BrandBlue,
  onPrimary = BrandWhite,
  primaryContainer = BrandBlueDark,
  secondary = ButtonSecondary,
  background = LightGray,
  surface = BrandWhite,
  onSurface = TextDark,
  onSurfaceVariant = TextSecondary
)
@Composable
fun Check24androidTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
