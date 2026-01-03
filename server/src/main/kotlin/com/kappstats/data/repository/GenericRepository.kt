package com.kappstats.data.repository

import com.kappstats.data.entity.Entity
import com.kappstats.data.remote.api.database.Database
import kotlin.reflect.KProperty1

interface GenericRepository<T, E: Entity> {
    val database: Database<E>
    suspend fun add(value: T): T?
    suspend fun delete(value: T): Boolean
    suspend fun deleteById(value: String): Boolean
    suspend fun getById(value: String): T?
    suspend fun getListByProperty(
        property: KProperty1<T, Any>,
        value: Any,
        limit: Int = 100,
        skip: Int = 0
    ): List<T>
    suspend fun getListByPropertyList(
        property: KProperty1<T, Any>,
        value: List<Any>,
        limit: Int = 100,
        skip: Int = 0
    ): List<T>
    suspend fun update(value: T): T?
}