package com.kappstats.presentation.screen.apps

import androidx.lifecycle.viewModelScope
import com.kappstats.presentation.core.view_model.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppsMonitorViewModel: StateViewModel() {

    private val _uiState = MutableStateFlow(AppsMonitorUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AppsMonitorEvent) {
        when(event) {
            is AppsMonitorEvent.SetDescription -> _uiState.update { it.copy(description = event.description) }
            is AppsMonitorEvent.SetName -> _uiState.update { it.copy(name = event.name) }
        }
    }

    fun add() {
        viewModelScope.launch {

        }
    }

}