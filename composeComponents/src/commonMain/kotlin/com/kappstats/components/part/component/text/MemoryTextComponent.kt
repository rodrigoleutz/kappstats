package com.kappstats.components.part.component.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.kappstats.components.theme.AppDimensions

@Composable
fun MemoryTextComponent(
    label: String,
    value: String,
    padding: Dp = AppDimensions.Small.component,
    labelColor: Color = Color.Black,
    valueColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = labelColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            maxLines = 1
        )
    }
}