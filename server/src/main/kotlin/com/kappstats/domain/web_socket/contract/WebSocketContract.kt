package com.kappstats.domain.web_socket.contract

import com.kappstats.domain.model.connection.ConnectionInfo
import com.kappstats.dto.web_socket.WsActionBase
import org.koin.core.component.KoinComponent

interface WebSocketContract<T, R>: KoinComponent {
    val base: WsActionBase<T, R>
    suspend fun process(connectionInfo: ConnectionInfo, data: T): R?
}