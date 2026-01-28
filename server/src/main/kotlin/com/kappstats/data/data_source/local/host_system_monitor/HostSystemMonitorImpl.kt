package com.kappstats.data.data_source.local.host_system_monitor

import com.kappstats.model.system_metrics.LinuxSystemMetrics
import com.kappstats.model.system_metrics.LoadAverage
import com.kappstats.model.system_metrics.NetworkInterfaceStats
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class HostSystemMonitorImpl(private val procPath: String = "/host_proc") : HostSystemMonitor {

    private val lastCpuTicksMap = ConcurrentHashMap<String, LongArray>()

    override fun getInfo(): LinuxSystemMetrics {
        val currentTicks = readAllCpuTicks()
        val cpuUsageMap = calculateCpuUsage(currentTicks)

        currentTicks.forEach { (name, ticks) -> lastCpuTicksMap[name] = ticks }

        val memInfo = readMemInfo()
        val loadAvg = readLoadAvg()

        return LinuxSystemMetrics(
            hostname = readSimpleFile("$procPath/sys/kernel/hostname"),
            kernelVersion = readSimpleFile("$procPath/version").split(" ").getOrElse(2) { "Unknown" },
            uptimeSeconds = readSimpleFile("$procPath/uptime").split(" ")[0].toDoubleOrNull()?.toLong() ?: 0L,

            cpuModel = readCpuModel(),
            cpuCoresPhysical = readPhysicalCores(),
            cpuCoresLogical = Runtime.getRuntime().availableProcessors(),
            cpuLoadAverage = loadAvg,
            cpuUsagePercent = cpuUsageMap["cpu"] ?: 0.0,
            coreUsagePercents = cpuUsageMap.filterKeys { it != "cpu" && it.startsWith("cpu") }
                .toSortedMap().values.toList(),

            memTotal = memInfo["MemTotal"] ?: 0,
            memAvailable = memInfo["MemAvailable"] ?: 0,
            memUsed = (memInfo["MemTotal"] ?: 0) - (memInfo["MemAvailable"] ?: 0),
            memFree = memInfo["MemFree"] ?: 0,
            memBuffers = memInfo["Buffers"] ?: 0,
            memCached = memInfo["Cached"] ?: 0,
            swapTotal = memInfo["SwapTotal"] ?: 0,
            swapUsed = (memInfo["SwapTotal"] ?: 0) - (memInfo["SwapFree"] ?: 0),

            interfaces = readNetworkStats(),

            totalProcesses = File("$procPath/stat").useLines { lines ->
                lines.firstOrNull { it.startsWith("processes") }?.split(Regex("\\s+"))?.lastOrNull()?.toIntOrNull() ?: 0
            },
            runningProcesses = parseRunningProcesses()
        )
    }

    private fun calculateCpuUsage(current: Map<String, LongArray>): Map<String, Double> {
        return current.mapValues { (name, currentTicks) ->
            val lastTicks = lastCpuTicksMap[name] ?: return@mapValues 0.0

            val totalDiff = currentTicks.sum() - lastTicks.sum()
            val idleDiff = currentTicks[3] - lastTicks[3] // index 3 é idle em /proc/stat

            if (totalDiff > 0) {
                (100.0 * (1.0 - (idleDiff.toDouble() / totalDiff))).coerceIn(0.0, 100.0)
            } else 0.0
        }
    }

    private fun readAllCpuTicks(): Map<String, LongArray> = File("$procPath/stat").useLines { lines ->
        lines.takeWhile { it.startsWith("cpu") }.associate { line ->
            val parts = line.trim().split(Regex("\\s+"))
            val name = parts[0]
            val ticks = LongArray(parts.size - 1) { i -> parts[i + 1].toLongOrNull() ?: 0L }
            name to ticks
        }
    }

    private fun readMemInfo(): Map<String, Long> = File("$procPath/meminfo").useLines { lines ->
        lines.associate { line ->
            val parts = line.split(":")
            val key = parts[0].trim()
            val value = parts[1].trim().split(" ")[0].toLongOrNull() ?: 0L
            key to value / 1024 // Convert KB to MB
        }
    }

    private fun readNetworkStats(): List<NetworkInterfaceStats> {
        val file = File("$procPath/net/dev")
        if (!file.exists()) return emptyList()
        return file.useLines { lines ->
            lines.drop(2).mapNotNull { line ->
                val parts = line.trim().split(Regex("\\s+"))
                if (parts.size < 12) return@mapNotNull null
                NetworkInterfaceStats(
                    name = parts[0].removeSuffix(":"),
                    rxBytes = parts[1].toLongOrNull() ?: 0L,
                    txBytes = parts[9].toLongOrNull() ?: 0L,
                    rxPackets = parts[2].toLongOrNull() ?: 0L,
                    txPackets = parts[10].toLongOrNull() ?: 0L,
                    rxErrors = parts[3].toLongOrNull() ?: 0L,
                    txErrors = parts[11].toLongOrNull() ?: 0L
                )
            }.toList()
        }
    }

    private fun readCpuModel() = File("$procPath/cpuinfo").useLines { lines ->
        lines.firstOrNull { it.startsWith("model name") }?.substringAfter(":")?.trim() ?: "Unknown"
    }

    private fun readPhysicalCores() = File("$procPath/cpuinfo").useLines { lines ->
        lines.filter { it.startsWith("core id") }.distinct().count().coerceAtLeast(1)
    }

    private fun readLoadAvg(): LoadAverage = try {
        val parts = readSimpleFile("$procPath/loadavg").split(" ")
        LoadAverage(parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
    } catch (e: Exception) { LoadAverage(0.0, 0.0, 0.0) }

    private fun parseRunningProcesses(): Int = try {
        readSimpleFile("$procPath/loadavg").split(" ")[3].split("/")[0].toInt()
    } catch (e: Exception) { 0 }

    private fun readSimpleFile(path: String) = try { File(path).readText().trim() } catch (e: Exception) { "" }
}