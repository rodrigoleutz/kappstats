package com.kappstats.data.remote.api.database

import com.kappstats.contracts.Model
import com.kappstats.data.entity.Entity
import kotlin.reflect.KProperty1

data class TableIndex<M: Model, T: Entity<M>>(
    val property: KProperty1<T, Any?>,
    val isAscending: Boolean = true,
    val isUnique: Boolean = false
)
