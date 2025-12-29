package com.kappstats.data.repository

import com.kappstats.contracts.Model
import com.kappstats.data.entity.Entity
import com.kappstats.data.remote.api.database.Database
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1

interface GenericRepository<M: @Serializable Model, E: Entity<M>> {
    val database: Database<E>
    suspend fun add(value: M): M?
    suspend fun delete(value: M): Boolean
    suspend fun deleteById(value: String): Boolean
    suspend fun getById(value: String): M?
    suspend fun getListByProperty(
        property: KProperty1<M, Any>,
        value: Any,
        limit: Int = 100,
        skip: Int = 0
    ): List<M>
    suspend fun getListByPropertyList(
        property: KProperty1<M, Any>,
        value: List<Any>,
        limit: Int = 100,
        skip: Int = 0
    ): List<M>
    suspend fun update(value: M): M?
}