package com.kappstats.domain.model.connection

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.ip_address.IpAddress

data class DefaultConnectionInfo(
    override val ipAddress: IpAddress,
    override val date: AppDateTime = AppDateTime.now
): ConnectionInfo
