package com.kappstats.components.part.widget.dashboard

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.kappstats.components.part.component.text.MemoryTextComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Green40

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
    swapTotalLabel: String,
    swapTotalValue: String,
    swapFreeLabel: String,
    swapFreeValue: String,
    modifier: Modifier = Modifier,
    padding: Dp = AppDimensions.Medium.component,
    innerPadding: Dp = AppDimensions.Medium.component
) {
    Card(
        modifier = modifier.padding(padding)
    ) {
        FlowRow(
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
                value = freeValue,
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
            Row(
                modifier = Modifier.weight(2f),
            ) {
                MemoryTextComponent(
                    modifier = Modifier.weight(1f),
                    label = swapTotalLabel,
                    value = swapTotalValue,
                    valueColor = Green40
                )
                MemoryTextComponent(
                    modifier = Modifier.weight(1f),
                    label = swapFreeLabel,
                    value = swapFreeValue,
                    valueColor = Green40
                )
            }
        }
    }
}