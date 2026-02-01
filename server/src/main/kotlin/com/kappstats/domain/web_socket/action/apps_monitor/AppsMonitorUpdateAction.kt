package com.kappstats.domain.web_socket.action.apps_monitor

import com.kappstats.custom_object.app_date_time.AppDateTime
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
object AppsMonitorUpdateAction: WebSocketContract<AppMonitor, AppMonitor?> {

    private val repository by inject<AppMonitorRepository>()

    override val base: WsActionBase<AppMonitor, AppMonitor?> =
        WebSocketEvents.Authenticate.AppsMonitor.Update

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: AppMonitor?
    ): WebSocketResponse? {
        return try {
            if(connectionInfo !is AuthConnectionInfo)
                return this.toFailure(WebSocketResponse.Companion.FailureType.Conflict)
            if(data == null)
                return this.toFailure(WebSocketResponse.Companion.FailureType.NoData)
            val appMonitor = repository.generic.getById(data.id)
                ?: return this.toFailure(WebSocketResponse.Companion.FailureType.LoadData)
            if(appMonitor.owner != connectionInfo.profileId)
                return this.toFailure(WebSocketResponse.Companion.FailureType.Unauthorized)
            val update = repository.generic.update(data.copy(
                updatedAt = appMonitor.updatedAt+listOf(AppDateTime.now)
            )) ?: return this.toFailure(WebSocketResponse.Companion.FailureType.Database)
            this.toSuccess(
                data = update,
                profileIdList = update.profileIdList
            )
        } catch (e: Exception) {
            e.printStackTrace()
            this.toFailure(WebSocketResponse.Companion.FailureType.Exception)
        }
    }
}