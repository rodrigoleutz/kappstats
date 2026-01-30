package com.kappstats.components.part.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.kappstats.components.part.widget.snackbar.AppSnackbarWidget
import com.kappstats.components.theme.AppDimensions

@Composable
fun CardMessageComponent(
    message: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    borderColor: Color = containerColor,
    borderSize: Dp = AppDimensions.Minimal.component,
    elevation: Dp = AppDimensions.Medium.component
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(borderSize, borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.Large.component)
        ) {
            Text(text = message, style = MaterialTheme.typography.titleLarge)
        }
    }
}