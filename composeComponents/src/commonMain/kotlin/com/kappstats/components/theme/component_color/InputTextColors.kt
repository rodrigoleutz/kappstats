package com.kappstats.components.theme.component_color

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Gray20
import com.kappstats.components.theme.Gray60
import com.kappstats.components.theme.Green40

object InputTextColors {


    @Composable
    fun outlinedInputTextColors() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Blue20,
        unfocusedBorderColor = Gray20,
        disabledBorderColor = Gray60,
        focusedLabelColor = Blue20,
        unfocusedLabelColor = Gray20,
        disabledLabelColor = Gray60
    )
}