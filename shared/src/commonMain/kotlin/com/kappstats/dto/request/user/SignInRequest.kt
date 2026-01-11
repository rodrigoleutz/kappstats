package com.kappstats.dto.request.user

import com.kappstats.Platform
import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.model.user.PlatformData
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: Email,
    val password: Password,
    val platform: PlatformData
)
