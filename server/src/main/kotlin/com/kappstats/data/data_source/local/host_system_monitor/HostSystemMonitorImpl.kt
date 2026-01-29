package com.kappstats.data.data_source.local.host_system_monitor

import com.kappstats.model.system_metrics.LinuxSystemMetrics
import com.kappstats.model.system_metrics.LoadAverage
import com.kappstats.model.system_metrics.NetworkInterfaceStats
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class HostSystemMonitorImpl(private val procPath: String = "/host_proc") : HostSystemMonitor {

    private val lastCpuTicksMap = ConcurrentHashMap<String, LongArray>()

    override fun getInfo(): LinuxSystemMetrics {
        val currentTicksAndProcesses = readAllCpuTicksAndProcesses()
        val cpuUsageMap = calculateCpuUsage(currentTicksAndProcesses.first)

        currentTicksAndProcesses.first.forEach { (name, ticks) -> lastCpuTicksMap[name] = ticks }

        val memInfo = readMemInfo()
        val loadAvgAndRunningProcess = readLoadAvgAndRunningProcess()
        val cpuModelAndCores = readModelAndPhysicalCores()
        return LinuxSystemMetrics(
            hostname = readSimpleFile("$procPath/sys/kernel/hostname"),
            kernelVersion = readSimpleFile("$procPath/version").split(" ")
                .getOrElse(2) { "Unknown" },
            uptimeSeconds = readSimpleFile("$procPath/uptime").split(" ")[0].toDoubleOrNull()
                ?.toLong() ?: 0L,

            cpuModel = cpuModelAndCores.first,
            cpuCoresPhysical = cpuModelAndCores.second,
            cpuCoresLogical = Runtime.getRuntime().availableProcessors(),
            cpuLoadAverage = loadAvgAndRunningProcess.first,
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

            totalProcesses = currentTicksAndProcesses.second,
            runningProcesses = loadAvgAndRunningProcess.second
        )
    }

    private fun calculateCpuUsage(current: Map<String, LongArray>): Map<String, Double> {
        return current.mapValues { (name, currentTicks) ->
            val lastTicks = lastCpuTicksMap[name] ?: return@mapValues 0.0
            val totalDiff = currentTicks.sum() - lastTicks.sum()
            val idleDiff = currentTicks[3] - lastTicks[3]
            if (totalDiff > 0) {
                (100.0 * (1.0 - (idleDiff.toDouble() / totalDiff))).coerceIn(0.0, 100.0)
            } else 0.0
        }
    }

    private fun readAllCpuTicksAndProcesses(): Pair<Map<String, LongArray>, Int> =
        File("$procPath/stat").useLines { lines ->
            val map = mutableMapOf<String, LongArray>()
            var processes = 0
            lines.forEach { line ->
                when {
                    line.startsWith("cpu") -> {
                        val parts = line.trim().split(Regex("\\s+"))
                        val ticks =
                            LongArray(parts.size - 1) { i -> parts[i + 1].toLongOrNull() ?: 0L }
                        map[parts[0]] = ticks
                    }

                    line.startsWith("processes") -> {
                        processes = line.split(Regex("\\s+")).lastOrNull()?.toIntOrNull() ?: 0
                    }
                }
            }
            map to processes
        }

    private fun readMemInfo(): Map<String, Long> = File("$procPath/meminfo").useLines { lines ->
        lines.associate { line ->
            val parts = line.split(":")
            val key = parts[0].trim()
            val value = parts[1].trim().split(" ")[0].toLongOrNull() ?: 0L
            key to value / 1024
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

    private fun readModelAndPhysicalCores() =
        File("$procPath/cpuinfo").useLines { lines ->
            var model = "Unknown"
            val coreIds = mutableSetOf<String>()
            lines.forEach { line ->
                when {
                    line.startsWith("model name") -> {
                        if (model == "Unknown") model = line.substringAfter(":").trim()
                    }
                    line.startsWith("core id") -> {
                        coreIds.add(line.substringAfter(":").trim())
                    }
                }
            }
            model to coreIds.size
        }

    private fun readLoadAvgAndRunningProcess(): Pair<LoadAverage, Int> = try {
        File("$procPath/loadavg").useLines { lines ->
            val parts = lines.first().split(" ")
            LoadAverage(
                parts[0].toDouble(),
                parts[1].toDouble(),
                parts[2].toDouble()
            ) to parts[3].split("/")[0].toInt()
        }
    } catch (e: Exception) {
        LoadAverage(0.0, 0.0, 0.0) to 0
    }


    private fun readSimpleFile(path: String) = try {
        File(path).readText().trim()
    } catch (e: Exception) {
        ""
    }
}