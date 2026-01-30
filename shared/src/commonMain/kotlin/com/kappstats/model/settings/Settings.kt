package com.kappstats.model.settings

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime

data class Settings(
    override val id: String,
    val authId: String,

    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
) : Model {

}
