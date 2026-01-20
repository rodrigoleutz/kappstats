package com.kappstats.presentation.screen.profile

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.kappstats.components.part.widget.snackbar.AppSnackbarVisuals
import com.kappstats.components.part.widget.snackbar.show
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.domain.web_socket.actions.user.AuthUserAuthUpdateAction
import com.kappstats.domain.web_socket.actions.user.AuthUserProfileUpdateAction
import com.kappstats.model.user.Auth
import com.kappstats.model.user.Profile
import com.kappstats.presentation.core.view_model.StateViewModel
import com.kappstats.resources.Res
import com.kappstats.resources.error_auth_update
import com.kappstats.resources.error_profile_update
import com.kappstats.resources.error_update
import com.kappstats.resources.error_username_invalid
import com.kappstats.resources.success_update
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@OptIn(FlowPreview::class)
class ProfileViewModel(
    private val authUseCases: AuthUseCases,
    private val profileUiState: ProfileUiState = ProfileUiState()
) : StateViewModel() {

    private val _uiState = MutableStateFlow(profileUiState)
    val uiState = _uiState.asStateFlow()

    private val hasChangedEmail: Boolean
        get() = stateHolder.dataState.user.value.myAuth?.email?.asString != uiState.value.email &&
                Email.isValidEmail(uiState.value.email)

    private val hasValidNewPassword: Boolean
        get() = uiState.value.newPassword.isNotBlank() &&
                Password.isValidPassword(uiState.value.newPassword) &&
                uiState.value.newPassword == uiState.value.newPasswordConfirm

    private val hasSameProfileProperties: Boolean
        get() = userState.myProfile?.name == uiState.value.name &&
                userState.myProfile?.username?.asString == uiState.value.username &&
                userState.myProfile?.bio == uiState.value.bio

    val enableUpdateAuth: Boolean
        get() = Password.isValidPassword(uiState.value.password) &&
                (hasChangedEmail && if (uiState.value.expandedChangePassword) hasValidNewPassword else true)

    val enabledUpdateProfile: Boolean
        get() = !uiState.value.loadingUsername &&
                userState.myProfile != null &&
                Username.isValidUsername(uiState.value.username) &&
                (uiState.value.hasUsername == null || uiState.value.hasUsername == false) &&
                !hasSameProfileProperties


    init {
        setCurrentProfileData()
        viewModelScope.launch {
            _uiState
                .map { it.username }
                .distinctUntilChanged()
                .onEach { username ->
                    if (username != userState.myProfile?.username?.asString &&
                        Username.isValidUsername(username)
                    ) setLoadingUsername(true)
                    setHasUsername(null)
                }
                .debounce(2_000)
                .filter { username ->
                    username != userState.myProfile?.username?.asString &&
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

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SetEmail -> _uiState.update { it.copy(email = event.email) }
            is ProfileEvent.SetExpandedChangePassword -> _uiState.update {
                it.copy(expandedChangePassword = event.expanded)
            }

            is ProfileEvent.SetBio -> _uiState.update { it.copy(bio = event.bio) }
            is ProfileEvent.SetName -> _uiState.update { it.copy(name = event.name) }
            is ProfileEvent.SetNewPassword -> _uiState.update { it.copy(newPassword = event.password) }
            is ProfileEvent.SetNewPasswordConfirm -> _uiState.update { it.copy(newPasswordConfirm = event.password) }
            is ProfileEvent.SetPassword -> _uiState.update { it.copy(password = event.password) }
            is ProfileEvent.SetUsername -> _uiState.update { it.copy(username = event.username) }
        }
    }

    fun setCurrentProfileData(profileData: Profile? = null, authData: Auth? = null) {
        val email = authData?.email?.asString ?: userState.myAuth?.email?.asString ?: "Error"
        userState.myProfile?.let { currentProfile ->
            _uiState.update {
                uiState.value.copy(
                    name = profileData?.name ?: currentProfile.name,
                    username = profileData?.username?.asString ?: currentProfile.username.asString,
                    bio = profileData?.bio ?: currentProfile.bio,
                    email = email
                )
            }
        }
    }

    fun setHasUsername(value: Boolean?) {
        _uiState.update { it.copy(hasUsername = value) }
    }

    fun setLoadingUsername(value: Boolean) {
        _uiState.update { it.copy(loadingUsername = value) }
    }

    fun updateAuth() {
        viewModelScope.launch {
            try {
                val email = Email(uiState.value.email)
                val password = Password(uiState.value.password)
                val newPassword =
                    if (uiState.value.expandedChangePassword &&
                        uiState.value.newPassword.isNotBlank()
                    ) Password(uiState.value.newPassword) else null
                val authData = AuthUserAuthUpdateAction.send(
                    Pair(Pair(email, newPassword), password)
                ) ?: run {
                    snackbarMessage(
                        getString(Res.string.error_update),
                        AppSnackbarVisuals.Type.Error
                    )
                    return@launch
                }
                delay(200)
                setCurrentProfileData(authData = authData)
                snackbarMessage(
                    getString(Res.string.success_update),
                    AppSnackbarVisuals.Type.Success
                )
            } catch (e: Exception) {
                snackbarMessage(
                    getString(Res.string.error_auth_update)
                )
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            val username = try {
                Username(uiState.value.username)
            } catch (e: Exception) {
                stateHolder.uiState.value.snackbarHostState.show(
                    getString(Res.string.error_username_invalid),
                    type = AppSnackbarVisuals.Type.Error
                )
                return@launch
            }
            userState.myProfile?.let { old ->
                val profile = old.copy(
                    name = uiState.value.name,
                    username = username,
                    bio = uiState.value.bio
                )
                val profileData = AuthUserProfileUpdateAction.send(profile) ?: run {
                    snackbarMessage(getString(Res.string.error_profile_update))
                    return@launch
                }
                delay(200)
                setCurrentProfileData(profileData = profileData)
                snackbarMessage(
                    getString(Res.string.success_update),
                    AppSnackbarVisuals.Type.Success
                )
            }
        }
    }
}