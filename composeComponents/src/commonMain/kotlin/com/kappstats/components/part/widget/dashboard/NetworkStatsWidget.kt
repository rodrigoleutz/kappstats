package com.kappstats.components.part.widget.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Green20
import com.kappstats.components.theme.Red20
import com.kappstats.model.system_metrics.NetworkInterfaceStats

@Composable
fun NetworkStatsWidget(
    label: String,
    rxBytes: String,
    txBytes: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component),
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppDimensions.Medium.component
        )
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.Medium.component)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = label,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = txBytes,
                    color = Green20,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = rxBytes,
                    textAlign = TextAlign.End,
                    color = Red20
                )
            }
        }
    }
}