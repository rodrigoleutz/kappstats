package com.kappstats.dto.web_socket


sealed interface WsActionType {
    val action: String?
    val isAuth: Boolean
        get() = false
}