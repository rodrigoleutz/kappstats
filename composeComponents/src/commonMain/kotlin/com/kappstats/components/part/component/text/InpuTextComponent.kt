package com.kappstats.components.part.component.text

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InputTextComponent(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        label = {
            Text(label)
        },
        value = value,
        onValueChange = {
            onChange(it)
        },
    )
}