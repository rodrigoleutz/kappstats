package com.kappstats.components.part.widget.dashboard

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.kappstats.components.part.component.progress.RadialGradientProgressComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Blue40
import com.kappstats.components.theme.Blue80
import com.kappstats.components.theme.Orange20
import com.kappstats.components.theme.Orange40
import com.kappstats.components.theme.Orange60
import com.kappstats.components.theme.Orange80

@Composable
fun CpuCoreUsageWidget(
    label: String,
    value: Double,
    modifier: Modifier = Modifier,
    cardRadialGradient: List<Color> = listOf(Blue20, Blue40),
    firstColor: Color = Blue20,
    secondColor: Color = Blue80,
    thirdColor: Color = Orange80,
    auxColor: Color = Orange60.copy(0.3f),
    gradientSize: Dp = AppDimensions.Large.image
) {
    val targetProgress = (value / 100).toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cpu_progress_anim"
    )
    Row {
        Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Card(
                modifier = Modifier.padding(AppDimensions.Large.component),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.Transparent
                ),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = AppDimensions.Medium.component
                )
            ) {
                Card(
                    modifier = Modifier.background(
                        Brush.radialGradient(
                            colors = cardRadialGradient
                        ),
                        shape = CircleShape
                    ),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.Transparent
                    ),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        RadialGradientProgressComponent(
                            modifier = Modifier,
                            progress = animatedProgress,
                            strokeWidth = AppDimensions.Large.component,
                            firstColor = firstColor,
                            secondColor = secondColor,
                            thirdColor = thirdColor,
                            auxColor = auxColor,
                            gradientSize = gradientSize
                        )
                        Text(
                            text = value.toInt().toString() + "%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        }
        Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
    }
}