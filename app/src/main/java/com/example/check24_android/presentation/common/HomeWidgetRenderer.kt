package com.example.check24_android.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.check24_android.domain.model.Widget

@Composable
fun HomeWidgetRenderer(
  widget: Widget,
  modifier: Modifier = Modifier,
  onCtaClick: (String) -> Unit
) {
    when (widget.componentType.lowercase()) {

        "carousel" -> CarouselWidget(widget.data, modifier, onCtaClick)

        "card" -> CardWidget(widget.data, modifier, onCtaClick)

        "banner" -> BannerWidget(widget.data, modifier, onCtaClick)
    }
}
