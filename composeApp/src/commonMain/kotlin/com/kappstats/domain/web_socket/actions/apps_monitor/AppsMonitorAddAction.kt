package com.kappstats.domain.web_socket.actions.apps_monitor

import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.app.AppMonitor

@WsAction
object AppsMonitorAddAction : WebSocketContract<AppMonitor, AppMonitor?> {

    override val base: WsActionBase<AppMonitor, AppMonitor?> =
        WebSocketEvents.Authenticate.AppsMonitor.Add

    override suspend fun process(value: AppMonitor?): AppMonitor? {
        value?.let { appMonitor ->
            val update = dataState.setAppsMonitorState(
                dataState.appsMonitor.value.copy(
                    mapAppsMonitor =
                        dataState.appsMonitor.value.mapAppsMonitor + (appMonitor.id to appMonitor)
                )
            )
        }
        return value
    }
}