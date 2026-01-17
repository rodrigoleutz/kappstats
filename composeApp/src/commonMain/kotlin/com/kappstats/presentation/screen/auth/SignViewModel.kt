package com.kappstats.presentation.screen.auth

import androidx.lifecycle.viewModelScope
import com.kappstats.components.part.widget.snackbar.AppSnackbarVisuals
import com.kappstats.components.part.widget.snackbar.show
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.dto.request.user.SignUpRequest
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.view_model.StateViewModel
import com.kappstats.resources.Res
import com.kappstats.resources.error_logout
import com.kappstats.resources.error_sign_in
import com.kappstats.resources.error_sign_up
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@OptIn(FlowPreview::class)
class SignViewModel(
    private val authUseCases: AuthUseCases
) : StateViewModel() {

    private val _uiState = MutableStateFlow(SignUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState
                .map { it.username }
                .distinctUntilChanged()
                .onEach {
                    if (Username.isValidUsername(it)) setLoadingUsername(true)
                    setHasUsername(null)
                }
                .debounce(2_000)
                .filter { username ->
                    username.isNotBlank() && Username.isValidUsername(username)
                }
                .collectLatest { username ->
                    val resource =
                        authUseCases.hasUsername.invoke(Username(username))
                    if (resource.isSuccess) {
                        setHasUsername(value = resource.asDataOrNull)
                    }
                    setLoadingUsername(false)
                }
        }
    }

    fun logOut(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val resource = authUseCases.logout.invoke()
            stateHolder.onMainEvent(MainEvent.SetIsLogged(!resource.isSuccess))
            if (!resource.isSuccess) stateHolder.uiState.value.snackbarHostState.show(
                getString(Res.string.error_logout),
                type = AppSnackbarVisuals.Type.Error
            )
            result(resource.isSuccess)
        }
    }

    fun onEvent(event: SignEvent) {
        when (event) {
            is SignEvent.SetEmail -> _uiState.update { it.copy(email = event.email) }
            is SignEvent.SetName -> _uiState.update { it.copy(name = event.name) }
            is SignEvent.SetPassword -> _uiState.update { it.copy(password = event.password) }
            is SignEvent.SetUsername -> _uiState.update { it.copy(username = event.username) }
        }
    }

    fun setHasUsername(value: Boolean?) {
        _uiState.update { it.copy(hasUsername = value) }
    }

    fun setLoadingUsername(value: Boolean) {
        _uiState.update { it.copy(loadingUsername = value) }
    }

    fun signIn(hasLoadingProgress: Boolean = true, result: (Boolean) -> Unit) {
        viewModelScope.launch {
            if(hasLoadingProgress) stateHolder.onMainEvent(MainEvent.SetIsLoading(true))
            try {
                val email = try {
                    Email(uiState.value.email)
                } catch (e: Exception) {
                    return@launch
                }
                val password = try {
                    Password(uiState.value.password)
                } catch (e: Exception) {
                    return@launch
                }
                val resource = authUseCases.signIn.invoke(email, password)
                stateHolder.onMainEvent(MainEvent.SetIsLogged(resource.isSuccess))
                if (!resource.isSuccess) {
                    stateHolder.uiState.value.snackbarHostState.show(
                        getString(Res.string.error_sign_in),
                        type = AppSnackbarVisuals.Type.Error
                    )
                } else {
                    val resourceAuthenticate = authUseCases.authenticate.invoke()
                    stateHolder.onMainEvent(MainEvent.SetIsLogged(resourceAuthenticate.isSuccess))
                }
                result(resource.isSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if(hasLoadingProgress) stateHolder.onMainEvent(MainEvent.SetIsLoading(false))
            }
        }
    }

    fun signUp(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            stateHolder.onMainEvent(MainEvent.SetIsLoading(true))
            try {
                val email = try {
                    Email(uiState.value.email)
                } catch (e: Exception) {
                    return@launch
                }
                val password = try {
                    Password(uiState.value.password)
                } catch (e: Exception) {
                    return@launch
                }
                val username = try {
                    Username(uiState.value.username)
                } catch (e: Exception) {
                    return@launch
                }
                val signUpRequest = SignUpRequest(
                    email = email,
                    username = username,
                    password = password,
                    name = uiState.value.name
                )
                val resource = authUseCases.signUp.invoke(signUpRequest)
                if (!resource.isSuccess) {
                    stateHolder.uiState.value.snackbarHostState.show(
                        getString(Res.string.error_sign_up),
                        type = AppSnackbarVisuals.Type.Error
                    )
                    result(resource.isSuccess)
                } else {
                    signIn(hasLoadingProgress = false) { result ->
                        if(!result) {
                            viewModelScope.launch {
                                stateHolder.uiState.value.snackbarHostState.show(
                                    getString(Res.string.error_sign_in),
                                    type = AppSnackbarVisuals.Type.Error
                                )
                            }
                        }
                        result(result)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stateHolder.onMainEvent(MainEvent.SetIsLoading(false))
            }
        }
    }

}