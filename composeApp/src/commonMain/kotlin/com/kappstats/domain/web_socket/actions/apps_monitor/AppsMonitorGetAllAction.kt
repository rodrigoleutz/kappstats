package com.kappstats.domain.web_socket.actions.apps_monitor

import co.touchlab.kermit.Logger
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.app.AppMonitor


@WsAction
object AppsMonitorGetAllAction : WebSocketContract<Any?, List<AppMonitor>> {

    override val base: WsActionBase<Any?, List<AppMonitor>> =
        WebSocketEvents.Authenticate.AppsMonitor.GetAll

    override suspend fun process(value: List<AppMonitor>): List<AppMonitor>? {
        val dataAdd = dataState.setAppsMonitorState(
            dataState.appsMonitor.value.copy(
                mapAppsMonitor = dataState.appsMonitor.value.mapAppsMonitor + value.associateBy { it.id }
            )
        )
        Logger.e { "$dataAdd" }
        return value
    }
}