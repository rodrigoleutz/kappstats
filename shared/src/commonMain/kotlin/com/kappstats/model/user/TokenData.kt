package com.kappstats.model.user

import com.kappstats.Platform
import com.kappstats.custom_object.app_date_time.AppDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TokenData(
    val id: String,
    val platform: Platform,
    val isActive: Boolean,
    val createdAt: AppDateTime = AppDateTime.now,
    val updatedAt: List<AppDateTime> = emptyList()
)
