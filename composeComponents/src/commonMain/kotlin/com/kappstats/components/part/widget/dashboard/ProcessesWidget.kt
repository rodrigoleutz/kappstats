package com.kappstats.components.part.widget.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.kappstats.components.theme.AppDimensions
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProcessesWidget(
    label: String,
    current: Long,
    total: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(AppDimensions.Medium.component)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$current/$total"
                )
            }
        }
    }
}