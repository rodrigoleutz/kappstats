package com.kappstats.data.repository

import com.kappstats.contracts.Model
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1

interface Repository<T: @Serializable Model> {
    suspend fun add(value: T): T?
    suspend fun delete(value: T): Boolean
    suspend fun deleteById(value: String): Boolean
    suspend fun getById(value: String): T?
    suspend fun <R> getListByProperty(
        property: KProperty1<T, R>,
        value: R,
        limit: Int = 100,
        skip: Int = 0
    ): List<T>
    suspend fun <R> getListByPropertyList(
        property: KProperty1<T, R>,
        value: List<R>,
        limit: Int = 100,
        skip: Int = 0
    ): List<T>
    suspend fun update(value: T): T?
}