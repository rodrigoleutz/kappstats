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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.kappstats.components.theme.AppDimensions
import compose.icons.EvaIcons
import compose.icons.TablerIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.AlertTriangle
import compose.icons.tablericons.Eye
import compose.icons.tablericons.EyeOff
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.min

@Composable
fun InputTextComponent(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    trailingIcon: (@Composable () -> Unit)? = null,
    minLines: Int = 1,
    maxLines: Int = 1,
    onChange: (String) -> Unit
) {
    var displayError by remember {
        mutableStateOf("")
    }
    var passwordVisibility by remember {
        mutableStateOf(false)
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
            minLines = minLines,
            maxLines = if(maxLines < minLines) minLines else maxLines,
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisibility) PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon = {
                if(trailingIcon != null) {
                    trailingIcon()
                } else {
                    when (keyboardType) {
                        KeyboardType.Password -> {
                            IconButton(
                                onClick = {
                                    passwordVisibility = !passwordVisibility
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisibility) TablerIcons.EyeOff else TablerIcons.Eye,
                                    contentDescription = "Password visibility"
                                )
                            }
                        }

                        else -> {}
                    }
                }
            },
            supportingText = {
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Row(
                        modifier = modifier,
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
            },
            colors = colors
        )
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