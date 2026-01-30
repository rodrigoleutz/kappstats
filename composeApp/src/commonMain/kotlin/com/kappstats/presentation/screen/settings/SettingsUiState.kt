package com.kappstats.presentation.screen.settings

data class SettingsUiState(
    val title: String = ""
)

sealed interface SettingsEvent {

}