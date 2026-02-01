package com.kappstats.domain.web_socket.actions.apps_monitor

import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase

@WsAction
object AppsMonitorDeleteAction : WebSocketContract<String, String?> {

    override val base: WsActionBase<String, String?> =
        WebSocketEvents.Authenticate.AppsMonitor.Delete

    override suspend fun process(value: String?): String? {
        val currentMap = dataState.appsMonitor.value.mapAppsMonitor
        val newMap = (value?.let { currentMap - it }) ?: return null
        dataState.setAppsMonitorState(
            dataState.appsMonitor.value.copy(mapAppsMonitor = newMap)
        )
        return value
    }

}