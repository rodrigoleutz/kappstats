package com.kappstats.dto.request.user

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: Email,
    val password: Password
)
