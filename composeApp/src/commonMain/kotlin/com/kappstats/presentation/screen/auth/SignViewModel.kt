package com.kappstats.presentation.screen.auth

import androidx.lifecycle.viewModelScope
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.domain.use_case.auth.AuthUseCases
import com.kappstats.presentation.core.navigation.AppScreens
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.view_model.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignViewModel(
    private val authUseCases: AuthUseCases
) : StateViewModel() {

    private val _uiState = MutableStateFlow(SignUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SignEvent) {
        when (event) {
            is SignEvent.SetEmail -> _uiState.update { it.copy(email = event.email) }
            is SignEvent.SetPassword -> _uiState.update { it.copy(password = event.password) }
        }
    }

    fun signIn(result: (Boolean) -> Unit) {
        viewModelScope.launch {
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
            result(resource.isSuccess)
        }
    }

}