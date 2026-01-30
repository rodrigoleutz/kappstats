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
import com.kappstats.util.IdGenerator
import org.bson.types.ObjectId
import org.koin.core.component.inject

@WsAction
object AppsMonitorAddAction : WebSocketContract<AppMonitor, AppMonitor?> {

    private val repository by inject<AppMonitorRepository>()

    override val base: WsActionBase<AppMonitor, AppMonitor?> =
        WebSocketEvents.Authenticate.AppsMonitor.Add

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: AppMonitor?
    ): WebSocketResponse? {
        return try {
            if (connectionInfo !is AuthConnectionInfo)
                return this.toFailure(WebSocketResponse.Companion.FailureType.Unauthorized)
            if (data == null) return this.toFailure(WebSocketResponse.Companion.FailureType.NoData)
            val dataToAdd = data.copy(
                id = ObjectId().toHexString(),
                hashId = IdGenerator.generateHashingId,
                owner = connectionInfo.profileId
            )
            val add = repository.generic.add(dataToAdd)
                ?: return this.toFailure(WebSocketResponse.Companion.FailureType.SaveData)
            this.toSuccess(
                data = add,
                profileIdList = listOf(connectionInfo.profileId)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            this.toFailure(WebSocketResponse.Companion.FailureType.Unknown)
        }
    }
}
