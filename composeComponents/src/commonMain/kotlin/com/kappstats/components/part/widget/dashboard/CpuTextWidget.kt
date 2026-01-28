package com.kappstats.components.part.widget.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.kappstats.components.theme.AppDimensions

@Composable
fun CpuTextWidget(
    label: String,
    value: String,
    padding: Dp = AppDimensions.Medium.component,
    innerPadding: Dp = AppDimensions.Medium.component,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(padding)
    ) {
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}