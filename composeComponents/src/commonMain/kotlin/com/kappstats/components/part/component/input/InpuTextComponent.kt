package com.kappstats.components.part.component.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kappstats.components.theme.AppDimensions
import compose.icons.EvaIcons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.AlertTriangle
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InputTextComponent(
    label: String,
    value: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    var displayError by remember {
        mutableStateOf("")
    }
    LaunchedEffect(errorMessage) {
        displayError = errorMessage ?: run {
            delay(1_000)
            ""
        }
    }
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
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Card(modifier = modifier) {
                Row(
                    modifier = modifier.padding(AppDimensions.Medium.component),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = EvaIcons.Fill.AlertTriangle,
                        contentDescription = displayError
                    )
                    Spacer(modifier = Modifier.width(AppDimensions.Small.component))
                    Text(displayError)
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