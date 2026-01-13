package com.kappstats.components.part.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Orange40
import kotlinx.coroutines.delay

fun Modifier.verticalScrollbar(
    state: LazyListState,
    width: Dp = AppDimensions.Small.component,
    color: Color = Orange40.copy(0.5f)
): Modifier = composed {

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset) {
        isVisible = true
        delay(5000) // 5 segundos
        isVisible = false
    }


    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "ScrollbarAlpha"
    )

    drawWithContent {
        drawContent()

        val layoutInfo = state.layoutInfo
        val visibleItems = layoutInfo.visibleItemsInfo

        if (visibleItems.isNotEmpty() && alpha > 0f) {
            val totalItemsCount = layoutInfo.totalItemsCount
            val viewportSize = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
            val averageSize = visibleItems.map { it.size }.average().toFloat()
            val estimatedTotalSize = averageSize * totalItemsCount

            if (estimatedTotalSize > viewportSize) {
                val scrollbarHeight = (viewportSize / estimatedTotalSize) * viewportSize
                val firstItem = visibleItems.first()
                val scrollOffset = (firstItem.index * averageSize) - firstItem.offset
                val scrollbarOffsetY = (scrollOffset / estimatedTotalSize) * viewportSize

                drawRect(
                    color = color,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                    size = Size(width.toPx(), scrollbarHeight),
                    alpha = alpha
                )
            }
        }
    }
}