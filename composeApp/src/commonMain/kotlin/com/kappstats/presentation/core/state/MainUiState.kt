package com.kappstats.presentation.core.state

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.NavKey

data class MainUiState(
    val hasTopBar: Boolean = false,
    val isBackButton: Boolean = false,
    val paddingValues: PaddingValues = PaddingValues(),
    val title: String = ""
)

sealed interface MainEvent {
    data object NavigatePop: MainEvent
    data class NavigatePush(val route: NavKey): MainEvent
    data class SetHasTopBar(val value: Boolean): MainEvent
    data class SetIsBackButton(val value: Boolean): MainEvent
    data class SetPaddingValues(val value: PaddingValues): MainEvent
    data class SetTitle(val value: String): MainEvent
}
