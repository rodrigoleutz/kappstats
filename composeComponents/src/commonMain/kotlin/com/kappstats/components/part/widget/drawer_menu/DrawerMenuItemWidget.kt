package com.kappstats.components.part.widget.drawer_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import com.kappstats.components.navigation.ComposeRoute
import com.kappstats.components.theme.AppDimensions
import org.jetbrains.compose.resources.stringResource

@Composable
fun DrawerMenuItemWidget(
    item: ComposeRoute,
    modifier: Modifier = Modifier,
    selected: Boolean,
    colors: DrawerMenuWidgetColors = DrawerMenuWidgetColors(),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().pointerHoverIcon(PointerIcon.Hand)
            .background(
                if (selected) colors.selectedItemContainerColor else Brush.verticalGradient(
                    listOf(colors.itemContainerColor, colors.itemContainerColor)
                )
            )
            .clickable {
                onClick()
            }.padding(AppDimensions.Large.component),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = stringResource(item.title),
            tint = if (selected) colors.selectedItemContentColor else colors.itemContentColor
        )
        Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
        Text(
            stringResource(item.title),
            color = if (selected) colors.selectedItemContentColor else colors.itemContentColor,
            fontWeight = FontWeight.Bold
        )
    }
}