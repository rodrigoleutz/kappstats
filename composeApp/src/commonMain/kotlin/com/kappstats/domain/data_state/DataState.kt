package com.kappstats.domain.data_state

import androidx.compose.runtime.State
import com.kappstats.domain.data_state.apps_monitor.AppsMonitorState
import com.kappstats.domain.data_state.user.UserState
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsActionType
import kotlinx.coroutines.flow.StateFlow

interface DataState {

    val appsMonitor: StateFlow<AppsMonitorState>
    val user: StateFlow<UserState>
    val errorState: State<String?>

    fun clearErrorState()

    fun setAppsMonitorState(value: AppsMonitorState): AppsMonitorState
    suspend fun setErrorState(failure: WebSocketResponse.Companion.FailureType)
    fun setUserState(value: UserState): UserState



}