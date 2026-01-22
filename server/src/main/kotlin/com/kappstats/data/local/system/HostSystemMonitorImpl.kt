package com.kappstats.data.local.system

import com.kappstats.model.system_metrics.LinuxSystemMetrics
import com.kappstats.model.system_metrics.LoadAverage
import com.kappstats.model.system_metrics.NetworkInterfaceStats
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.io.File


class HostSystemMonitorImpl(private val procPath: String = "/host_proc") : HostSystemMonitor {

    override fun collect(period: Long): Flow<LinuxSystemMetrics> = flow {
        while (currentCoroutineContext().isActive) {
            delay(period * 1_000)
            emit(getInfo())
        }
    }

    private fun getInfo(): LinuxSystemMetrics {
        val memInfo = readMemInfo()
        val loadAvg = readLoadAvg()
        val cpuInfo = readCpuInfo()
        val allCpuStats = readAllCpuTicks()
        val cpuUsageMap = calculateCpuUsage(allCpuStats)

        return LinuxSystemMetrics(
            hostname = File("$procPath/sys/kernel/hostname").readText().trim(),
            kernelVersion = File("$procPath/version").readText().split(" ")[2],
            uptimeSeconds = File("$procPath/uptime").readText().split(" ")[0].toDouble().toLong(),

            cpuModel = cpuInfo.first,
            cpuCoresPhysical = cpuInfo.second,
            cpuCoresLogical = Runtime.getRuntime().availableProcessors(),
            cpuLoadAverage = loadAvg,
            cpuUsagePercent = cpuUsageMap["cpu"] ?: 0.0,
            coreUsagePercents = cpuUsageMap.filterKeys { it.startsWith("cpu") && it != "cpu" }
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

            totalProcesses = File("$procPath/stat").useLines {
                it.firstOrNull { l -> l.startsWith("processes") }?.split(" ")?.last()?.toInt() ?: 0
            },
            runningProcesses = File("$procPath/loadavg").readText()
                .split(" ")[3].split("/")[0].toInt()
        )
    }

    private fun calculateCpuUsage(currentTicksMap: Map<String, LongArray>): Map<String, Double> {
        val usageMap = mutableMapOf<String, Double>()
        val lastCpuTicksMap = mutableMapOf<String, LongArray>()
        currentTicksMap.forEach { (name, current) ->
            val last = lastCpuTicksMap[name]
            if (last != null) {
                val idleDiff = current[3] - last[3]
                val totalDiff = current.sum() - last.sum()
                val usage =
                    if (totalDiff > 0) 100.0 * (1.0 - (idleDiff.toDouble() / totalDiff)) else 0.0
                usageMap[name] = usage
            } else {
                usageMap[name] = 0.0
            }
            lastCpuTicksMap[name] = current
        }
        return usageMap
    }

    private fun readAllCpuTicks(): Map<String, LongArray> {
        return File("$procPath/stat").useLines { lines ->
            lines.filter { it.startsWith("cpu") }
                .associate { line ->
                    val parts = line.split(Regex("\\s+"))
                    val name = parts[0]
                    val ticks =
                        parts.drop(1).filter { it.isNotEmpty() }.map { it.toLong() }.toLongArray()
                    name to ticks
                }
        }
    }

    private fun readCpuInfo(): Pair<String, Int> {
        val lines = File("$procPath/cpuinfo").readLines()
        val model = lines.firstOrNull { it.startsWith("model name") }?.substringAfter(":")?.trim()
            ?: "Unknown"
        val cores = lines.filter { it.startsWith("cpu cores") }.distinct().size
        return model to cores
    }

    private fun readLoadAvg(): LoadAverage {
        val parts = File("$procPath/loadavg").readText().split(" ")
        return LoadAverage(parts[0].toDouble(), parts[1].toDouble(), parts[2].toDouble())
    }

    private fun readMemInfo() = File("$procPath/meminfo").useLines { lines ->
        lines.associate { line ->
            val parts = line.split(":")
            parts[0] to parts[1].trim().split(" ")[0].toLong() / 1024
        }
    }

    private fun readNetworkStats(): List<NetworkInterfaceStats> {
        return File("$procPath/net/dev").useLines { lines ->
            lines.drop(2).map { line ->
                val parts = line.trim().split(Regex("\\s+"))
                NetworkInterfaceStats(
                    name = parts[0].removeSuffix(":"),
                    rxBytes = parts[1].toLong(),
                    txBytes = parts[9].toLong(),
                    rxPackets = parts[2].toLong(),
                    txPackets = parts[10].toLong(),
                    rxErrors = parts[3].toLong(),
                    txErrors = parts[11].toLong()
                )
            }.toList()
        }
    }
}