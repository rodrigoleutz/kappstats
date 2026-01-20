package com.kappstats.model.app

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime

data class AppMonitor(
    override val id: String,
    val owner: String,
    val name: String,
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): Model
