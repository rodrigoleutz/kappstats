package com.kappstats.model.message

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime

data class Message(
    override val id: String,
    val toProfileId: String,
    val title: String,
    val message: String,
    val fromAppMonitor: String? = null,
    val fromProfileId: String? = null,
    val read: AppDateTime? = null,
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): Model
