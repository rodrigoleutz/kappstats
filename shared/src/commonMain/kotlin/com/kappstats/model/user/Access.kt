package com.kappstats.model.user

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime

data class Access(
    override val id: String,
    val authId: String,

    override val createdAt: AppDateTime,
    override val updatedAt: List<AppDateTime>
): Model
