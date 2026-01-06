package com.kappstats.domain.web_socket.action.status

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.domain.web_socket.contract.WebSocketContract
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WsAction
import com.kappstats.dto.web_socket.WsActionBase

@WsAction
object StatusPingAction: WebSocketContract<AppDateTime, AppDateTime> {
    override val base: WsActionBase<AppDateTime, AppDateTime> =
        WebSocketEvents.Status.Ping

}