package com.kappstats.domain.data_state

import com.kappstats.domain.data_state.user.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet

class DataStateImpl: DataState {

    private val _user = MutableStateFlow(UserState())
    override val user: StateFlow<UserState> = _user.asStateFlow()

    override fun setUserState(value: UserState): UserState {
        return _user.updateAndGet { value }
    }

}