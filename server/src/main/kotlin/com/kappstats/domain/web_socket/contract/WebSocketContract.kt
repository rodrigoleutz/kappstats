package com.kappstats.domain.web_socket.contract

import com.kappstats.dto.web_socket.WsActionBase

interface WebSocketContract<T, R> {
    val base: WsActionBase<T, R>
    fun process(data: T): R?
}