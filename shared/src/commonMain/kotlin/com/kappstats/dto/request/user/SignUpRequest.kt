package com.kappstats.dto.request.user

import com.kappstats.custom_object.email.Email
import com.kappstats.custom_object.password.Password
import com.kappstats.custom_object.username.Username
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: Email,
    val password: Password,
    val name: String,
    val username: Username
) {

    init {
        require(isValid(this)) { "Invalid name, string is blank." }
    }

    companion object {
        fun isValid(signUpRequest: SignUpRequest): Boolean {
            return signUpRequest.name.isNotBlank()
        }
    }
}
