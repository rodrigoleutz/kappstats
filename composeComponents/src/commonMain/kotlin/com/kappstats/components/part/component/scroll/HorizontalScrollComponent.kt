package com.kappstats.components.part.component.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun HorizontalScrollComponent(
    scrollState: LazyListState,
    modifier: Modifier = Modifier
)