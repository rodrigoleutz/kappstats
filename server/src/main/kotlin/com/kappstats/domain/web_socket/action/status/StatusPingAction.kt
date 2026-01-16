package com.kappstats.domain.web_socket.action.status

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketRequest
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase

@WsAction
object StatusPingAction: WebSocketContract<AppDateTime, AppDateTime> {
    override val base: WsActionBase<AppDateTime, AppDateTime> =
        WebSocketEvents.Status.Ping

    override suspend fun WebSocketRequest.process(
        connectionInfo: ConnectionInfo,
        data: AppDateTime?
    ): WebSocketResponse? {
        return this.toSuccess(AppDateTime.now)
    }
}