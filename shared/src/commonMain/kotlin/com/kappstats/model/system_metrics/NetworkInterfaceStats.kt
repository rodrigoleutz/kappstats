package com.kappstats.model.system_metrics

import kotlinx.serialization.Serializable

@Serializable
data class NetworkInterfaceStats(
    val name: String,
    val rxBytes: Long,
    val txBytes: Long,
    val rxPackets: Long,
    val txPackets: Long,
    val rxErrors: Long,
    val txErrors: Long
)