package com.kappstats.presentation.screen.profile

data class ProfileUiState(
    val email: String = "",
    val expandedChangePassword: Boolean = false,
    val hasUsername: Boolean? = null,
    val loadingUsername: Boolean = false,
    val name: String = "",
    val newPassword: String = "",
    val newPasswordConfirm: String = "",
    val password: String = "",
    val username: String = "",
    val bio: String = ""
)

sealed interface ProfileEvent {
    data class SetEmail(val email: String) : ProfileEvent
    data class SetExpandedChangePassword(val expanded: Boolean) : ProfileEvent
    data class SetName(val name: String) : ProfileEvent
    data class SetNewPassword(val password: String) : ProfileEvent
    data class SetNewPasswordConfirm(val password: String) : ProfileEvent
    data class SetPassword(val password: String) : ProfileEvent
    data class SetUsername(val username: String) : ProfileEvent
    data class SetBio(val bio: String) : ProfileEvent
}
