package com.kappstats.data.remote.api.database

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.koin.core.component.KoinComponent
import kotlin.reflect.KProperty1

interface Database<T : Any> : KoinComponent {
    val collection: MongoCollection<T>
    suspend fun add(item: T): T?
    suspend fun addMany(items: List<T>): List<T>?
    suspend fun getById(id: String, limit: Int = 100, skip: Int = 0): T?
    suspend fun update(id: String, item: T): T?

    suspend fun delete(id: String): Boolean
    suspend fun deleteByProperty(property: KProperty1<T, Any?>, value: Any?): Boolean

    suspend fun getByProperty(property: KProperty1<T, Any?>, value: Any?): T?
    suspend fun getListByProperty(
        property: KProperty1<T, Any?>,
        value: Any?,
        limit: Int = 100,
        skip: Int = 0,
    ): List<T>

    suspend fun <R> getListByPropertyList(
        property: KProperty1<T, R>,
        value: List<R>,
        limit: Int = 100,
        skip: Int = 0,
    ): List<T>

    suspend fun getListByPropertyContains(
        property: KProperty1<T, Any?>,
        value: Any?,
        limit: Int = 100,
        skip: Int = 0,
    ): List<T>

    suspend fun getAllWithLimitAndSkip(limit: Int = 100, skip: Int = 0): List<T>

    suspend fun <V : Any> setField(id: String, fieldProperty: KProperty1<T, V>, value: V): Boolean

    suspend fun <E : Any> pushToList(
        id: String,
        listProperty: KProperty1<T, List<E>>,
        element: E,
    ): Boolean

    suspend fun <E : Any> pullFromList(
        id: String,
        listProperty: KProperty1<T, List<E>>,
        element: E,
    ): Boolean

    suspend fun <E : Any> updateListItemByIndex(
        id: String,
        listProperty: KProperty1<T, List<E>>,
        index: Int,
        newElement: E,
    ): Boolean
}
