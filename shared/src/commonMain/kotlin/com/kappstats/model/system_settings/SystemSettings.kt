package com.kappstats.model.system_settings

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime

data class SystemSettings(
    override val id: String,

    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): Model
