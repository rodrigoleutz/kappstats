package com.kappstats.presentation.screen.profile

import androidx.lifecycle.viewModelScope
import com.kappstats.components.part.widget.snackbar.AppSnackbarVisuals
import com.kappstats.components.part.widget.snackbar.show
import com.kappstats.custom_object.username.Username
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.domain.web_socket.actions.user.AuthUserProfileUpdateAction
import com.kappstats.model.user.Profile
import com.kappstats.presentation.core.view_model.StateViewModel
import com.kappstats.resources.Res
import com.kappstats.resources.error_username
import com.kappstats.resources.error_username_invalid
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
import kotlinx.coroutines.withTimeoutOrNull
import org.jetbrains.compose.resources.getString

@OptIn(FlowPreview::class)
class ProfileViewModel(
    private val authUseCases: AuthUseCases
) : StateViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    val profile = stateHolder.dataState.user.value.myProfile

    private var oldProfile: Profile? = null

    val enabledUpdate: Boolean
        get() = !uiState.value.loadingUsername && oldProfile != null &&
                (Username.isValidUsername(uiState.value.username) &&
                        (uiState.value.hasUsername == null || uiState.value.hasUsername == false)) &&
                oldProfile != oldProfile?.copy(
            name = uiState.value.name,
            username = Username(uiState.value.username),
            bio = uiState.value.bio
        )


    init {
        setCurrentProfileData()
        viewModelScope.launch {
            _uiState
                .map { it.username }
                .distinctUntilChanged()
                .onEach { username ->
                    if (username != profile?.username?.asString && Username.isValidUsername(username))
                        setLoadingUsername(true)
                    setHasUsername(null)
                }
                .debounce(2_000)
                .filter { username ->
                    username != profile?.username?.asString &&
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
            is ProfileEvent.SetBio -> _uiState.update { it.copy(bio = event.bio) }
            is ProfileEvent.SetName -> _uiState.update { it.copy(name = event.name) }
            is ProfileEvent.SetUsername -> _uiState.update { it.copy(username = event.username) }
        }
    }

    fun setCurrentProfileData() {
        oldProfile = profile
        profile?.let { currentProfile ->
            _uiState.update {
                uiState.value.copy(
                    name = currentProfile.name,
                    username = currentProfile.username.asString,
                    bio = currentProfile.bio
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

    fun updateProfile(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val username = try {
                Username(uiState.value.username)
            } catch (e: Exception) {
                stateHolder.uiState.value.snackbarHostState.show(
                    getString(Res.string.error_username_invalid),
                    type = AppSnackbarVisuals.Type.Error
                )
                result(false)
                return@launch
            }
            stateHolder.user.value.myProfile?.let { old ->
                val profile = old.copy(
                    name = uiState.value.name,
                    username = username,
                    bio = uiState.value.bio
                )
                val result = AuthUserProfileUpdateAction.send(profile)
                result(true)
            }
        }
    }
}