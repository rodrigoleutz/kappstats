package com.kappstats.domain.data_state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kappstats.domain.core.FailureType
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
    private val userState: UserState = UserState()
): DataState {

    private val _user = MutableStateFlow(userState)
    override val user: StateFlow<UserState> = _user.asStateFlow()

    private val _errorState = mutableStateOf<String?>(null)
    override val errorState: State<String?> = _errorState

    override fun clearErrorState() {
        _errorState.value = null
    }

    override suspend fun setErrorState(
        failure: WebSocketResponse.Companion.FailureType
    ) {
        _errorState.value = getString(failure.getFailureString())
    }

    override fun setUserState(value: UserState): UserState {
        return _user.updateAndGet { value }
    }
}