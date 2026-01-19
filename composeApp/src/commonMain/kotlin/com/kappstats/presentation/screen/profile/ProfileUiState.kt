package com.kappstats.presentation.screen.profile

data class ProfileUiState(
    val email: String = "",
    val hasUsername: Boolean? = null,
    val loadingUsername: Boolean = false,
    val name: String = "",
    val username: String = "",
    val bio: String = ""
)

sealed interface ProfileEvent {
    data class SetEmail(val email: String): ProfileEvent
    data class SetName(val name: String): ProfileEvent
    data class SetUsername(val username: String): ProfileEvent
    data class SetBio(val bio: String): ProfileEvent
}
