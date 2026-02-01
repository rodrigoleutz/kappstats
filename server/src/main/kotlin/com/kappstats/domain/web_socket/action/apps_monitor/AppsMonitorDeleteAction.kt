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
import org.koin.core.component.inject

@WsAction
object AppsMonitorDeleteAction : WebSocketContract<String, String?> {

    private val repository: AppMonitorRepository by inject()

    override val base: WsActionBase<String, String?> =
        WebSocketEvents.Authenticate.AppsMonitor.Delete

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: String?
    ): WebSocketResponse? {
        return try {
            if (connectionInfo !is AuthConnectionInfo)
                return this.toFailure(WebSocketResponse.Companion.FailureType.Conflict)
            if (data.isNullOrBlank())
                return this.toFailure(WebSocketResponse.Companion.FailureType.NoData)
            val appMonitor = repository.generic.getById(data)
                ?: return this.toFailure(WebSocketResponse.Companion.FailureType.LoadData)
            if (appMonitor.owner != connectionInfo.profileId)
                return this.toFailure(WebSocketResponse.Companion.FailureType.Unauthorized)
            val delete = repository.generic.deleteById(data)
            if (!delete) return this.toFailure(WebSocketResponse.Companion.FailureType.Database)
            this.toSuccess(
                data = data,
                profileIdList = appMonitor.profileIdList
            )
        } catch (e: Exception) {
            e.printStackTrace()
            this.toFailure(WebSocketResponse.Companion.FailureType.Exception)
        }
    }
}