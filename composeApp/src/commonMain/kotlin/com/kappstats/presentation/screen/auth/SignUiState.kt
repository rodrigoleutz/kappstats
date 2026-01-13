package com.kappstats.presentation.screen.auth

data class SignUiState(
    val email: String = "",
    val hasUsername: Boolean? = null,
    val loadingUsername: Boolean = false,
    val name: String = "",
    val password: String = "",
    val username: String = ""
)

sealed interface SignEvent {
    data class SetEmail(val email: String): SignEvent
    data class SetName(val name: String): SignEvent
    data class SetPassword(val password: String): SignEvent
    data class SetUsername(val username: String): SignEvent
}
