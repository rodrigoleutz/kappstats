package com.kappstats.contracts

import com.kappstats.custom_object.app_date_time.AppDateTime

interface Model {
    val id: String
    val createdAt: AppDateTime
    val updatedAt: List<AppDateTime>
}