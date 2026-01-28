package com.kappstats.presentation.screen.dashboard

import androidx.lifecycle.viewModelScope
import com.kappstats.domain.use_case.dashboard.DashboardUseCases
import com.kappstats.model.dashboard.Dashboard
import com.kappstats.presentation.core.state.MainEvent
import com.kappstats.presentation.core.view_model.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardUseCases: DashboardUseCases
): StateViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        stateHolder.onMainEvent(MainEvent.SetIsLoading(true))
        viewModelScope.launch {
            try {
                dashboardUseCases.collectInfo.invoke().collect { dashboard ->
                    setDashboard(dashboard)
                }
            } catch (e: Exception) {
                stateHolder.onMainEvent(MainEvent.SetIsLoading(false))
            }
        }
    }

    fun setDashboard(value: Dashboard) {
        stateHolder.onMainEvent(MainEvent.SetIsLoading(false))
        _uiState.update { it.copy(dashboard = value) }
    }

}