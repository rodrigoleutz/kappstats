package com.kappstats.data.data_source.remote.api.database

import com.kappstats.data.data_source.entity.Entity
import kotlin.reflect.KProperty1

data class TableIndex<T: Entity>(
    val property: KProperty1<T, Any?>,
    val isAscending: Boolean = true,
    val isUnique: Boolean = false
)
