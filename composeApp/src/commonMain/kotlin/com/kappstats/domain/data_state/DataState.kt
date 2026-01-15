package com.kappstats.domain.data_state

import com.kappstats.domain.data_state.user.UserState
import kotlinx.coroutines.flow.StateFlow

interface DataState {

    val user: StateFlow<UserState>

    fun setUserState(value: UserState): UserState
}