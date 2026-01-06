package com.kappstats.dto.web_socket

interface WsActionType {
    val action: String?
    val isAuth: Boolean
        get() = false
}