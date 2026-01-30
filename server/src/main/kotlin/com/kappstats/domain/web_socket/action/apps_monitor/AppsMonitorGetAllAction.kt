package com.kappstats.domain.web_socket.action.apps_monitor

import com.kappstats.data.repository.app.AppMonitorRepository
import com.kappstats.domain.model.connection.AuthConnectionInfo
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase
import com.kappstats.model.app.AppMonitor
import org.koin.core.component.inject

@WsAction
object AppsMonitorGetAllAction : WebSocketContract<Any?, List<AppMonitor>> {

    private val repository by inject<AppMonitorRepository>()

    override val base: WsActionBase<Any?, List<AppMonitor>> =
        WebSocketEvents.Authenticate.AppsMonitor.GetAll

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: Any?
    ): WebSocketResponse? {
        return try {
            if (connectionInfo !is AuthConnectionInfo)
                return this.toFailure(WebSocketResponse.Companion.FailureType.Unauthorized)
            val list = repository.getListByProfileId(connectionInfo.profileId)
            this.toSuccess(
                data = list,
                profileIdList = listOf(connectionInfo.profileId)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            this.toFailure(WebSocketResponse.Companion.FailureType.Unknown)
        }
    }
}