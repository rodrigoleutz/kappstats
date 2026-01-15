package com.kappstats.domain.data_state.user

import com.kappstats.model.user.Auth
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.Profile

data class UserState(
    val myAuth: Auth? = null,
    val myAuthToken: AuthToken? = null,
    val myProfile: Profile? = null,
    val authMap: Map<String, Auth> = emptyMap(),
    val authTokenMap: Map<String, AuthToken> = emptyMap(),
    val profileMap: Map<String, Profile> = emptyMap(),
)
