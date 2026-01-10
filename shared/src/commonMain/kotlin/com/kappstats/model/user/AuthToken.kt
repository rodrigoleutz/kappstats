package com.kappstats.model.user

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime

data class AuthToken(
    override val id: String,
    val authId: String,
    val tokens: List<TokenData> = emptyList(),
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): Model
