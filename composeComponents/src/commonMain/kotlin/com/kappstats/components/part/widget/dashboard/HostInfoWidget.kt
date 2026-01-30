package com.kappstats.components.part.widget.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kappstats.components.theme.AppDimensions

@Composable
fun HostInfoWidget(
    hostname: String,
    kernelVersion: String,
    uptime: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.padding(AppDimensions.Medium.component),
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
                    text = hostname,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = kernelVersion
                )
            }
            Text(
                modifier = Modifier,
                text = uptime,
                textAlign = TextAlign.End
            )
        }
    }

}