package com.kappstats.components.part.component.progress

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Blue80
import com.kappstats.components.theme.Orange60
import com.kappstats.components.theme.Orange80

@Composable
fun RadialGradientProgressComponent(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = AppDimensions.Large.component
) {

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Spacer(
        modifier = modifier
            .size(150.dp)
            .drawWithCache {
                val gradientBrush = Brush.sweepGradient(
                    0.0f to Blue20,
                    0.5f to Blue80,
                    1.0f to Orange80,
                    center = Offset(size.width / 2, size.height / 2)
                )

                onDrawWithContent {
                    drawArc(
                        color = Orange60.copy(0.3f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                    )


                    rotate(degrees = -90f) {
                        drawArc(
                            brush = gradientBrush,
                            startAngle = 0f,
                            sweepAngle = animatedProgress * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
                        )
                    }
                }
            }
    )
}