package com.kappstats.presentation.screen.profile.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.container.ScrollableContainerComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Orange20
import com.kappstats.components.theme.Orange80
import com.kappstats.components.theme.Red20
import com.kappstats.components.theme.component_color.InputTextColors
import com.kappstats.custom_object.username.Username
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.profile.ProfileEvent
import com.kappstats.presentation.screen.profile.ProfileUiState
import com.kappstats.resources.Res
import com.kappstats.resources.bio
import com.kappstats.resources.cancel
import com.kappstats.resources.error_username
import com.kappstats.resources.error_username_already_exists
import com.kappstats.resources.name
import com.kappstats.resources.update
import com.kappstats.resources.username
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.Save
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileProfileScreen(
    mainUiState: MainUiState,
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    enableUpdate: Boolean,
    onUpdate: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableContainerComponent<Unit>(
        modifier = modifier.fillMaxSize(),
        paddingValues = mainUiState.paddingValues
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component)
        ) {
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.name),
                value = uiState.name,
                onChange = {
                    onEvent(ProfileEvent.SetName(it))
                },
                colors = InputTextColors.outlinedInputTextColors()
            )
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.username),
                value = uiState.username,
                onChange = {
                    onEvent(ProfileEvent.SetUsername(it))
                },
                keyboardType = KeyboardType.Text,
                errorMessage = when (uiState.hasUsername) {
                    true -> stringResource(Res.string.error_username_already_exists)
                    false -> null
                    else -> {
                        if (Username.isValidUsername(uiState.username)) null
                        else stringResource(Res.string.error_username)
                    }
                },
                trailingIcon = {
                    AnimatedVisibility(
                        uiState.loadingUsername,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(AppDimensions.ExtraLarge.component),
                            trackColor = Orange80,
                            color = Orange20
                        )
                    }
                },
                colors = InputTextColors.outlinedInputTextColors()
            )
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.bio),
                value = uiState.bio,
                minLines = 3,
                maxLines = 5,
                onChange = {
                    onEvent(ProfileEvent.SetBio(it))
                },
                colors = InputTextColors.outlinedInputTextColors()
            )
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.cancel),
                    icon = EvaIcons.Fill.Close,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red20,
                        contentColor = Color.White
                    ),
                    enabled = true,
                    onClick = {
                        onCancel()
                    }
                )
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.update),
                    icon = EvaIcons.Fill.Save,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue20,
                        contentColor = Color.White
                    ),
                    enabled = enableUpdate,
                    onClick = {
                        onUpdate()
                    }
                )
            }
        }
    }
}