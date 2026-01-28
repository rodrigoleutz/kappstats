package com.kappstats.domain.model.connection

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.ip_address.IpAddress

data class DashboardConnectionInfo(
    override val webSocketId: String,
    override val ipAddress: IpAddress,
    val authId: String,
    val profileId: String,
    val tokenId: String,
    override val date: AppDateTime = AppDateTime.now
) : ConnectionInfo {

    companion object {

        fun fromAuthConnectionInfo(
            authConnectionInfo: AuthConnectionInfo?
        ): DashboardConnectionInfo? {
            return authConnectionInfo?.let {
                try {
                    DashboardConnectionInfo(
                        webSocketId = authConnectionInfo.webSocketId,
                        ipAddress = authConnectionInfo.ipAddress,
                        authId = authConnectionInfo.authId,
                        profileId = authConnectionInfo.profileId,
                        tokenId = authConnectionInfo.tokenId,
                        date = authConnectionInfo.date
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}
