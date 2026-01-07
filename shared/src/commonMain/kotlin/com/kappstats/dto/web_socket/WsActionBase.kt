package com.kappstats.dto.web_socket

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

open class WsActionBase<T, R>(
    val parent: WsActionType?,
    val command: String,
    val inputSerializer: KSerializer<@Serializable T>? = null,
    val outputSerializer: KSerializer<@Serializable R>? = null,
    override val isAuth: Boolean = false
): WsActionType {
    override val action: String by lazy {
        (parent?.action ?: "") + command
    }
}