package com.kappstats.components.part.widget.loading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Orange40
import com.kappstats.components.theme.Orange80
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingScreenWidget(
    loadingText: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Blue20.copy(0.9f),
    progressColor: Color = Orange80,
    trackColor: Color = Orange40,
    textColor: Color = Orange80
) {
    Box(
        modifier = Modifier.fillMaxSize().then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            ),
            border = BorderStroke(width = AppDimensions.Minimal.component, color = progressColor)
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.Large.component),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = progressColor,
                    trackColor = trackColor
                )
                Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
                Text(text = loadingText, color = textColor)
            }
        }
    }
}