package com.kappstats.data.repository

import com.kappstats.data.entity.Entity
import com.kappstats.data.remote.api.database.Database
import kotlin.reflect.KProperty1

class GenericRepositoryImpl<E : Entity>(
    override val database: Database<E>
) : GenericRepository<E, E> {

    override suspend fun add(value: E): E? = database.add(value)

    override suspend fun delete(value: E): Boolean = database.delete(value._id.toHexString())

    override suspend fun deleteById(value: String): Boolean = database.delete(value)

    override suspend fun getById(value: String): E? = database.getById(value)

    override suspend fun getListByProperty(
        property: KProperty1<E, Any>,
        value: Any,
        limit: Int,
        skip: Int
    ): List<E> = database.getListByProperty(property, value, limit, skip)

    override suspend fun getListByPropertyList(
        property: KProperty1<E, Any>,
        value: List<Any>,
        limit: Int,
        skip: Int
    ): List<E> = database.getListByProperty(property, value, limit, skip)

    override suspend fun update(value: E): E? = database.update(value._id.toHexString(), value)
}