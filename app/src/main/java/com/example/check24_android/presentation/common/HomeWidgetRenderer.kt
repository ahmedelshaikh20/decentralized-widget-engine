package com.example.check24_android.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.check24_android.domain.model.Widget
import com.example.check24_android.presentation.model.BannerResolvedWidget
import com.example.check24_android.presentation.model.CardResolvedWidget
import com.example.check24_android.presentation.model.CarouselResolvedWidget
import com.example.check24_android.presentation.model.ResolvedWidget

@Composable
fun HomeWidgetRenderer(
  widget: ResolvedWidget,
  modifier: Modifier = Modifier,
  onCtaClick: (String) -> Unit
) {
  when (widget) {
    is CarouselResolvedWidget -> CarouselWidget(widget.payload, modifier, onCtaClick)

    is CardResolvedWidget -> CardWidget(widget.payload, modifier, onCtaClick)

    is BannerResolvedWidget -> BannerWidget(widget.payload, modifier, onCtaClick)
  }
}


