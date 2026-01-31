package com.kappstats.presentation.screen.apps

import com.kappstats.model.app.AppMemberType
import com.kappstats.model.app.AppMonitor

data class AppsMonitorUiState(
    val description: String = "",
    val editAppMonitor: AppMonitor? = null,
    val members: Map<String, AppMemberType> = emptyMap(),
    val name: String = ""
)

sealed interface AppsMonitorEvent {
    data class Delete(val id: String): AppsMonitorEvent
    data class SetName(val name: String): AppsMonitorEvent
    data class SetDescription(val description: String): AppsMonitorEvent
}