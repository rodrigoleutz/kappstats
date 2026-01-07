package com.kappstats.dto.web_socket

import com.kappstats.custom_object.app_date_time.AppDateTime

object WebSocketEvents : WsActionBase<Any?, Any?>(null, "/web_socket") {

    object Status: WsActionBase<Any?, Any?>(WebSocketEvents, "/status") {

        object Ping: WsActionBase<AppDateTime, AppDateTime>(
            parent = Status,
            command = "/ping",
            inputSerializer = AppDateTime.serializer(),
            outputSerializer = AppDateTime.serializer(),
            isAuth = false
        )
    }
}