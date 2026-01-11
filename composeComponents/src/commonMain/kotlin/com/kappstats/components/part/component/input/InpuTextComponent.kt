package com.kappstats.components.part.component.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kappstats.components.theme.AppDimensions
import compose.icons.EvaIcons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.AlertTriangle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InputTextComponent(
    label: String,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = modifier,
            label = {
                Text(label)
            },
            value = value,
            onValueChange = {
                onChange(it)
            },
        )
        AnimatedVisibility(errorMessage != null) {
            errorMessage?.let {
                Card(modifier = modifier) {
                    Row(
                        modifier = modifier.padding(AppDimensions.Medium.component),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = EvaIcons.Fill.AlertTriangle,
                            contentDescription = it
                        )
                        Spacer(modifier = Modifier.width(AppDimensions.Small.component))
                        Text(it)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InputTextComponentPreview(modifier: Modifier = Modifier) {
    InputTextComponent(
        label = "Test",
        value = "",
        onChange = {}
    )
}