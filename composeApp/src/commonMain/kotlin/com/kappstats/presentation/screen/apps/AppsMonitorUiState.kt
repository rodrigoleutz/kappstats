package com.kappstats.presentation.screen.apps

import com.kappstats.model.app.AppMemberType
import com.kappstats.model.app.AppMonitor

data class AppsMonitorUiState(
    val appsList: List<AppMonitor> = emptyList(),
    val name: String = "",
    val description: String = "",
    val members: Map<String, AppMemberType> = emptyMap()
)

sealed interface AppsMonitorEvent {
    data class SetName(val name: String): AppsMonitorEvent
    data class SetDescription(val description: String): AppsMonitorEvent
}