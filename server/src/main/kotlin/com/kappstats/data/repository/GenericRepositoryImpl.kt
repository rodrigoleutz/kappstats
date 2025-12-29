package com.kappstats.data.repository

import com.kappstats.contracts.Model
import com.kappstats.data.entity.Entity
import com.kappstats.data.entity.EntityMapper
import com.kappstats.data.remote.api.database.Database
import kotlin.reflect.KProperty1

class GenericRepositoryImpl<M : Model, E : Entity<M>>(
    private val entityMapper: EntityMapper<M, E>,
    override val database: Database<E>
) : GenericRepository<M, E> {

    override suspend fun add(value: M): M? {
        return try {
            val entityValue = entityMapper.fromModel(value) as E
            database.add(entityValue)?.toModel()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun delete(value: M): Boolean {
        return try {
            database.delete(value.id)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteById(value: String): Boolean {
        return try {
            database.delete(value)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getById(value: String): M? {
        return try {
            database.getById(value)?.toModel()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getListByProperty(
        property: KProperty1<M, Any>,
        value: Any,
        limit: Int,
        skip: Int
    ): List<M> {
        return try {
            val entityProperty = entityMapper.propertyFromModel(property) as KProperty1<E, Any>
            return database.getListByProperty(entityProperty, value, limit, skip)
                .mapNotNull { it.toModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getListByPropertyList(
        property: KProperty1<M, Any>,
        value: List<Any>,
        limit: Int,
        skip: Int
    ): List<M> {
        return try {
            val entityProperty = entityMapper.propertyFromModel(property) as KProperty1<E, Any>
            val entityValueList =
                value.map { entityMapper.valueFromModelProperty(property, value) }
            return database.getListByPropertyList(entityProperty, entityValueList, limit, skip)
                .mapNotNull { it.toModel() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun update(value: M): M? {
        return try {
            val entityValue = entityMapper.fromModel(value) as E
            return database.update(value.id, entityValue)?.toModel()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}