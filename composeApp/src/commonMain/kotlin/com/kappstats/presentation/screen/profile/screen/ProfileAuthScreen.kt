package com.kappstats.presentation.screen.profile.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.part.widget.switch.SwitchWidget
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Orange40
import com.kappstats.components.theme.Orange60
import com.kappstats.components.theme.Red20
import com.kappstats.components.theme.component_color.InputTextColors
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.presentation.constants.Tags
import com.kappstats.presentation.core.state.MainUiState
import com.kappstats.presentation.screen.profile.ProfileEvent
import com.kappstats.presentation.screen.profile.ProfileUiState
import com.kappstats.resources.Res
import com.kappstats.resources.cancel
import com.kappstats.resources.change_password
import com.kappstats.resources.current_password_required
import com.kappstats.resources.email
import com.kappstats.resources.error_email
import com.kappstats.resources.error_password
import com.kappstats.resources.error_password_confirm
import com.kappstats.resources.new_password
import com.kappstats.resources.new_password_confirm
import com.kappstats.resources.password
import com.kappstats.resources.save
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.Save
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileAuthScreen(
    mainUiState: MainUiState,
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    updateEnabled: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    var updateClick by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize()
            .padding(AppDimensions.Medium.component)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateTopPadding()))
        InputTextComponent(
            modifier = Modifier.fillMaxWidth().testTag(Tags.EMAIL),
            label = stringResource(Res.string.email),
            value = uiState.email,
            onChange = {
                onEvent(ProfileEvent.SetEmail(it))
            },
            errorMessage = if (Email.isValidEmail(uiState.email)) null
            else stringResource(Res.string.error_email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            maxLines = 1,
            colors = InputTextColors.outlinedInputTextColors(),
        )
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            SwitchWidget(
                modifier = Modifier.testTag(Tags.EXPAND_BUTTON),
                value = uiState.expandedChangePassword,
                onChange = {
                    onEvent(ProfileEvent.SetExpandedChangePassword(it))
                },
                label = stringResource(Res.string.change_password),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Blue20,
                    checkedThumbColor = Orange60,
                    checkedBorderColor = Orange40
                )
            )
        }
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        AnimatedVisibility(uiState.expandedChangePassword) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(AppDimensions.Medium.component)
                ) {
                    InputTextComponent(
                        modifier = Modifier.fillMaxWidth().testTag(Tags.NEW_VALUE),
                        label = stringResource(Res.string.new_password),
                        value = uiState.newPassword,
                        onChange = {
                            onEvent(ProfileEvent.SetNewPassword(it))
                        },
                        colors = InputTextColors.outlinedInputTextColors(),
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        errorMessage = if (uiState.newPassword.isBlank() || Password.isValidPassword(
                                uiState.newPassword
                            )
                        ) null
                        else stringResource(Res.string.error_password),
                        maxLines = 1
                    )
                    InputTextComponent(
                        modifier = Modifier.fillMaxWidth().testTag(Tags.NEW_VALUE_CONFIRM),
                        label = stringResource(Res.string.new_password_confirm),
                        value = uiState.newPasswordConfirm,
                        onChange = {
                            onEvent(ProfileEvent.SetNewPasswordConfirm(it))
                        },
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        colors = InputTextColors.outlinedInputTextColors(),
                        errorMessage = if (uiState.newPassword.isBlank() ||
                            uiState.newPasswordConfirm == uiState.newPassword &&
                            Password.isValidPassword(uiState.newPassword)
                        ) null
                        else stringResource(Res.string.error_password_confirm),
                        maxLines = 1
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
        InputTextComponent(
            modifier = Modifier.fillMaxWidth().testTag(Tags.PASSWORD),
            label = stringResource(Res.string.password),
            value = uiState.password,
            onChange = {
                onEvent(ProfileEvent.SetPassword(it))
            },
            colors = InputTextColors.outlinedInputTextColors(),
            keyboardType = KeyboardType.Password,
            maxLines = 1,
            errorMessage = if (uiState.password.isBlank()) stringResource(Res.string.current_password_required)
            else if (!Password.isValidPassword(uiState.password)) stringResource(Res.string.error_password)
            else null
        )
        Spacer(modifier = Modifier.height(AppDimensions.ExtraLarge.component))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ButtonComponent(
                modifier = Modifier.weight(1f).testTag(Tags.CANCEL),
                label = stringResource(Res.string.cancel),
                icon = EvaIcons.Fill.Close,
                onClick = {
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red20,
                    contentColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
            ButtonComponent(
                modifier = Modifier.weight(1f).testTag(Tags.CONFIRM),
                label = stringResource(Res.string.save),
                icon = EvaIcons.Fill.Save,
                onClick = {
                    onSave()
                    updateClick = !updateClick
                },
                enabled = updateEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue20,
                    contentColor = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.height(mainUiState.paddingValues.calculateBottomPadding()))
    }
}