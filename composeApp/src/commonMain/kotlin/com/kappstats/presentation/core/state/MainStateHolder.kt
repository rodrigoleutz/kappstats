package com.kappstats.presentation.core.state

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainStateHolder {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    var onBackAction: (() -> Unit)? = null
    var onNavigate: ((NavKey) -> Unit)? = null

    fun navPop() {
        onBackAction?.invoke()
    }

    fun navPush(route: NavKey) {
        onNavigate?.invoke(route)
    }

    fun onMainEvent(event: MainEvent) {
        when (event) {
            MainEvent.NavigatePop -> navPop()
            is MainEvent.NavigatePush -> navPush(event.route)
            is MainEvent.SetHasTopBar -> _uiState.update { it.copy(hasTopBar = event.value) }
            is MainEvent.SetIsBackButton -> {
                if(!event.value) navPop()
                _uiState.update { it.copy(isBackButton = event.value) }
            }
            is MainEvent.SetPaddingValues -> _uiState.update { it.copy(paddingValues = event.value) }
            is MainEvent.SetTitle -> _uiState.update { it.copy(title = event.value) }
        }
    }

}