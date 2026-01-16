package com.kappstats.presentation.screen.auth.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.kappstats.components.part.component.button.ButtonComponent
import com.kappstats.components.part.component.container.ScrollableContainerComponent
import com.kappstats.components.part.component.input.InputTextComponent
import com.kappstats.components.part.modifier.verticalScrollbar
import com.kappstats.components.theme.AppDimensions
import com.kappstats.components.theme.Blue20
import com.kappstats.components.theme.Green20
import com.kappstats.components.theme.Orange20
import com.kappstats.components.theme.Orange60
import com.kappstats.components.theme.Orange80
import com.kappstats.components.theme.Red20
import com.kappstats.components.theme.component_color.InputTextColors
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.screen.auth.SignEvent
import com.kappstats.presentation.screen.auth.SignUiState
import com.kappstats.resources.Res
import com.kappstats.resources.app_name
import com.kappstats.resources.clear
import com.kappstats.resources.email
import com.kappstats.resources.error_email
import com.kappstats.resources.error_password
import com.kappstats.resources.error_password_confirm
import com.kappstats.resources.error_username
import com.kappstats.resources.error_username_already_exists
import com.kappstats.resources.login
import com.kappstats.resources.logo
import com.kappstats.resources.name
import com.kappstats.resources.password
import com.kappstats.resources.password_confirm
import com.kappstats.resources.register
import com.kappstats.resources.username
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Close
import compose.icons.evaicons.fill.LogIn
import compose.icons.evaicons.fill.PersonAdd
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignUpScreen(
    paddingValues: PaddingValues,
    uiState: SignUiState,
    onSignUp: () -> Unit,
    onEvent: (SignEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordConfirm by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        onMainEvent(MainEvent.SetHasTopBar(true))
    }
    val listState = rememberLazyListState()
    ScrollableContainerComponent<Unit>(
        listState = listState,
        modifier = Modifier.verticalScrollbar(listState),
        paddingValues = paddingValues
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(AppDimensions.Medium.component),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(AppDimensions.Large.image),
                painter = painterResource(Res.drawable.logo),
                contentDescription = stringResource(Res.string.app_name)
            )
            Spacer(modifier = Modifier.height(AppDimensions.ExtraLarge.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.name),
                value = uiState.name,
                onChange = {
                    onEvent(SignEvent.SetName(it))
                },
                keyboardType = KeyboardType.Text,
                colors = InputTextColors.outlinedInputTextColors()
            )
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.email),
                value = uiState.email,
                onChange = {
                    onEvent(SignEvent.SetEmail(it))
                },
                keyboardType = KeyboardType.Email,
                errorMessage = if (uiState.email.isBlank() || Email.isValidEmail(uiState.email)) null
                else stringResource(Res.string.error_email),
                colors = InputTextColors.outlinedInputTextColors()
            )
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.username),
                value = uiState.username,
                onChange = {
                    onEvent(SignEvent.SetUsername(it))
                },
                keyboardType = KeyboardType.Text,
                errorMessage = when (uiState.hasUsername) {
                    true -> stringResource(Res.string.error_username_already_exists)
                    false -> null
                    else -> {
                        if (uiState.username.isBlank() || Username.isValidUsername(uiState.username)) null
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
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.password),
                value = uiState.password,
                onChange = {
                    onEvent(SignEvent.SetPassword(it))
                },
                keyboardType = KeyboardType.Password,
                errorMessage = if (uiState.password.isBlank() || Password.isValidPassword(
                        uiState.password
                    )
                ) null
                else stringResource(Res.string.error_password),
                colors = InputTextColors.outlinedInputTextColors()
            )
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            InputTextComponent(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.password_confirm),
                value = passwordConfirm,
                onChange = {
                    passwordConfirm = it
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSignUp()
                        defaultKeyboardAction(ImeAction.Done)
                    }
                ),
                errorMessage =
                    if (passwordConfirm.isBlank() || uiState.password == passwordConfirm) null
                    else stringResource(Res.string.error_password_confirm),
                colors = InputTextColors.outlinedInputTextColors()
            )
            Spacer(modifier = Modifier.height(AppDimensions.Medium.component))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.clear),
                    icon = EvaIcons.Fill.Close,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red20,
                        contentColor = Color.White
                    ),
                    enabled = uiState.email.isNotBlank() ||
                            uiState.password.isNotBlank() ||
                            uiState.name.isNotBlank() ||
                            passwordConfirm.isNotBlank(),
                    onClick = {
                        onEvent(SignEvent.SetName(""))
                        onEvent(SignEvent.SetEmail(""))
                        onEvent(SignEvent.SetPassword(""))
                    }
                )
                Spacer(modifier = Modifier.width(AppDimensions.Medium.component))
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.register),
                    icon = EvaIcons.Fill.PersonAdd,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue20,
                        contentColor = Color.White
                    ),
                    enabled = Email.isValidEmail(uiState.email) &&
                            Username.isValidUsername(uiState.username) &&
                            Password.isValidPassword(uiState.password) &&
                            passwordConfirm == uiState.password &&
                            uiState.name.isNotBlank() &&
                            uiState.hasUsername == false,
                    onClick = {
                        onSignUp()
                    }
                )
            }
        }
    }
}