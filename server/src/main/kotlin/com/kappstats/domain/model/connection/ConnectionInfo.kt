package com.kappstats.domain.model.connection

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.ip_address.IpAddress


interface ConnectionInfo {
    val ipAddress: IpAddress
    val date: AppDateTime
}
