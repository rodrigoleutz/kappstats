package com.kappstats.domain.data_state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kappstats.domain.core.FailureType
import com.kappstats.domain.data_state.apps_monitor.AppsMonitorState
import com.kappstats.domain.data_state.user.UserState
import com.kappstats.domain.web_socket.util.getFailureString
import com.kappstats.dto.web_socket.WebSocketEvents
import com.kappstats.dto.web_socket.WebSocketResponse
import com.kappstats.dto.web_socket.WsActionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import org.jetbrains.compose.resources.getString

class DataStateImpl(
    private val userState: UserState = UserState(),
    private val appsMonitorState: AppsMonitorState = AppsMonitorState()
): DataState {

    private val _user = MutableStateFlow(userState)
    override val user: StateFlow<UserState> = _user.asStateFlow()
    private val _appsMonitor = MutableStateFlow(appsMonitorState)
    override val appsMonitor: StateFlow<AppsMonitorState> = _appsMonitor.asStateFlow()
    private val _errorState = mutableStateOf<String?>(null)
    override val errorState: State<String?> = _errorState

    override fun clearErrorState() {
        _errorState.value = null
    }

    override fun setAppsMonitorState(value: AppsMonitorState): AppsMonitorState =
        _appsMonitor.updateAndGet { value }

    override suspend fun setErrorState(
        failure: WebSocketResponse.Companion.FailureType
    ) {
        _errorState.value = getString(failure.getFailureString())
    }

    override fun setUserState(value: UserState): UserState =
        _user.updateAndGet { value }
}