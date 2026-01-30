package com.kappstats.presentation.screen.settings

import com.kappstats.presentation.core.view_model.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel: StateViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when(event) {
            else -> {}
        }
    }
}