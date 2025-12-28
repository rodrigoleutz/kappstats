package com.kappstats.model.user

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.username.Username
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    override val id: String,
    val name: String,
    val username: Username,
    val bio: String,
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): Model
