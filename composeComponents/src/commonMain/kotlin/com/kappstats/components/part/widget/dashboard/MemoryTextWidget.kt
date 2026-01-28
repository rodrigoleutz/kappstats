package com.kappstats.components.part.widget.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.kappstats.components.part.component.text.MemoryTextComponent
import com.kappstats.components.theme.AppDimensions

@Composable
fun MemoryTextWidget(
    totalLabel: String,
    totalValue: String,
    freeLabel: String,
    freeValue: String,
    usedLabel: String,
    usedValue: String,
    cachedLabel: String,
    cachedValue: String,
    modifier: Modifier = Modifier,
    padding: Dp = AppDimensions.Medium.component,
    innerPadding: Dp = AppDimensions.Medium.component
) {
    Card(
        modifier = modifier.padding(padding)
    ) {
        Row(
            modifier = Modifier.padding(innerPadding)
        ) {
            MemoryTextComponent(
                modifier = Modifier.weight(1f),
                label = totalLabel,
                value = totalValue
            )
            MemoryTextComponent(
                modifier = Modifier.weight(1f),
                label = freeLabel,
                value = freeValue
            )
            MemoryTextComponent(
                modifier = Modifier.weight(1f),
                label = usedLabel,
                value = usedValue
            )
            MemoryTextComponent(
                modifier = Modifier.weight(1f),
                label = cachedLabel,
                value = cachedValue
            )
        }
    }
}