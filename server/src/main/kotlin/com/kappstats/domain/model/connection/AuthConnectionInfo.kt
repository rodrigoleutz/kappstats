package com.kappstats.domain.model.connection

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.ip_address.IpAddress

data class AuthConnectionInfo(
    override val ipAddress: IpAddress,
    val authId: String,
    val profileId: String,
    val tokenId: String,
    override val date: AppDateTime = AppDateTime.now
): ConnectionInfo
