package com.kappstats.domain.data_state.apps_monitor

import com.kappstats.model.app.AppMonitor

data class AppsMonitorState(
    val mapAppsMonitor: Map<String, AppMonitor> = emptyMap()
)
