package com.kappstats.model.system_metrics

import kotlinx.serialization.Serializable

@Serializable
data class LoadAverage(
    val oneMin: Double,
    val fiveMin: Double,
    val fifteenMin: Double
)