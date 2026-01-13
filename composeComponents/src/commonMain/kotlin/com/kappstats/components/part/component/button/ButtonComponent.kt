package com.kappstats.components.part.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import com.kappstats.components.theme.AppDimensions

@Composable
fun ButtonComponent(
    label: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
    elevation: Dp = AppDimensions.Medium.component,
    pointerHover: PointerIcon = if(enabled) PointerIcon.Hand else PointerIcon.Default,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.pointerHoverIcon(pointerHover),
        onClick = {
            onClick()
        },
        colors = colors,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            disabledElevation = AppDimensions.None.component,
            pressedElevation = AppDimensions.None.component
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = label
                )
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            }
            Text(label)
        }
    }
}