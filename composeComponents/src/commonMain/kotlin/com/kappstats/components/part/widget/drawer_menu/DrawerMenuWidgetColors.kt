package com.kappstats.components.part.widget.drawer_menu

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class DrawerMenuWidgetColors(
    val containerColor: Brush = Brush.verticalGradient(
        listOf(
            Color.Transparent,
            Color.Transparent
        )
    ),
    val contentColor: Color = Color.Black,
    val itemContentColor: Color = Color.Black,
    val itemContainerColor: Color = Color.Transparent,
    val selectedItemContainerColor: Brush = Brush.verticalGradient(
        listOf(
            Color.Black,
            Color.Black
        )
    ),
    val selectedItemContentColor: Color = Color.White
)
