package com.kappstats.domain.web_socket.util

import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.resources.Res
import com.kappstats.resources.failure_conflict
import com.kappstats.resources.failure_database
import com.kappstats.resources.failure_exception
import com.kappstats.resources.failure_load_data
import com.kappstats.resources.failure_network
import com.kappstats.resources.failure_no_data
import com.kappstats.resources.failure_save_data
import com.kappstats.resources.failure_serialization
import com.kappstats.resources.failure_unauthorized
import com.kappstats.resources.failure_unknown
import org.jetbrains.compose.resources.StringResource

fun WebSocketResponse.Companion.FailureType.getFailureString(): StringResource {
    return when(this) {
        WebSocketResponse.Companion.FailureType.Conflict -> Res.string.failure_conflict
        WebSocketResponse.Companion.FailureType.Database -> Res.string.failure_database
        WebSocketResponse.Companion.FailureType.Exception -> Res.string.failure_exception
        WebSocketResponse.Companion.FailureType.LoadData -> Res.string.failure_load_data
        WebSocketResponse.Companion.FailureType.Network -> Res.string.failure_network
        WebSocketResponse.Companion.FailureType.NoData -> Res.string.failure_no_data
        WebSocketResponse.Companion.FailureType.SaveData -> Res.string.failure_save_data
        WebSocketResponse.Companion.FailureType.Serialization -> Res.string.failure_serialization
        WebSocketResponse.Companion.FailureType.Unauthorized -> Res.string.failure_unauthorized
        WebSocketResponse.Companion.FailureType.Unknown -> Res.string.failure_unknown
    }
}