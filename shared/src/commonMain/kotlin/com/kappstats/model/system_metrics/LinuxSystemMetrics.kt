package com.kappstats.model.system_metrics

import kotlinx.serialization.Serializable

@Serializable
data class LinuxSystemMetrics(
    val hostname: String,
    val kernelVersion: String,
    val uptimeSeconds: Long,

    val cpuModel: String,
    val cpuCoresPhysical: Int,
    val cpuCoresLogical: Int,
    val cpuUsagePercent: Double,
    val coreUsagePercents: List<Double>,
    val cpuLoadAverage: LoadAverage,

    val memTotal: Long,
    val memAvailable: Long,
    val memUsed: Long,
    val memFree: Long,
    val memBuffers: Long,
    val memCached: Long,
    val swapTotal: Long,
    val swapUsed: Long,

    val interfaces: List<NetworkInterfaceStats>,

    val totalProcesses: Int,
    val runningProcesses: Int
)

