package com.kappstats.components.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppDimensions(
    val component: Dp,
    val image: Dp,
    val font: TextUnit
) {
    None(
        component = 0.dp,
        image = 0.dp,
        font = 0.sp
    ),
    Minimal(
        component = 1.dp,
        image = 24.dp,
        font = 10.sp
    ),
    Smallest(
        component = 2.dp,
        image = 32.dp,
        font = 14.sp
    ),
    Small(
        component = 4.dp,
        image = 64.dp,
        font = 18.sp
    ),
    Medium(
        component = 8.dp,
        image = 128.dp,
        font = 20.sp
    ),
    Large(
        component = 16.dp,
        image = 256.dp,
        font = 24.sp
    ),
    ExtraLarge(
        component = 32.dp,
        image = 512.dp,
        font = 32.sp
    ),
    ExtraExtraLarge(
        component = 64.dp,
        image = 1024.dp,
        font = 64.sp
    )
}
