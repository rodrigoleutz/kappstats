package com.kappstats.presentation.screen.auth

data class SignUiState(
    val email: String = "",
    val password: String = ""
)

sealed interface SignEvent {
    data object SignIn: SignEvent
    data class SetEmail(val email: String): SignEvent
    data class SetPassword(val password: String): SignEvent
}
