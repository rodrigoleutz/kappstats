package com.kappstats.model.user

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.email.Email
import kotlinx.serialization.Serializable

@Serializable
data class Auth(
    override val id: String,
    val email: Email,
    val profileId: String,
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): Model
