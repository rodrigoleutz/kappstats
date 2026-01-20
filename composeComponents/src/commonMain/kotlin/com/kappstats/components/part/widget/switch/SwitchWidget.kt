package com.kappstats.components.part.widget.switch

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import com.kappstats.components.theme.AppDimensions

@Composable
fun SwitchWidget(
    value: Boolean,
    label: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    colors: SwitchColors = SwitchDefaults.colors(),
    onChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).then(modifier),
            checked = value,
            onCheckedChange = {
                onChange(it)
            },
            enabled = enabled,
            colors = colors
        )
        label?.let {
            Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            Text(
                text = label,
                color = colors.checkedTrackColor,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}