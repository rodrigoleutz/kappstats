package com.kappstats.data.repository

import com.kappstats.contracts.Model
import com.kappstats.data.entity.EntityWithModel
import com.kappstats.data.entity.EntityMapper
import com.kappstats.data.remote.api.database.Database
import kotlin.reflect.KProperty1

class GenericRepositoryImpl<M : Model, E : EntityWithModel<M>>(
    private val entityMapper: EntityMapper<M, E>,
    override val database: Database<E>
) : GenericRepository<M, E> {

    override suspend fun add(value: M): M? {
        val entityValue = entityMapper.fromModel(value) as E
        return database.add(entityValue)?.toModel()
    }

    override suspend fun delete(value: M): Boolean = database.delete(value.id)
    override suspend fun deleteById(value: String): Boolean = database.delete(value)
    override suspend fun getById(value: String): M? = database.getById(value)?.toModel()

    override suspend fun getListByProperty(
        property: KProperty1<M, Any>,
        value: Any,
        limit: Int,
        skip: Int
    ): List<M> {
        val entityProperty = entityMapper.propertyFromModel(property) as KProperty1<E, Any>
        return database.getListByProperty(entityProperty, value, limit, skip)
            .mapNotNull { it.toModel() }
    }

    override suspend fun getListByPropertyList(
        property: KProperty1<M, Any>,
        value: List<Any>,
        limit: Int,
        skip: Int
    ): List<M> {
        val entityProperty = entityMapper.propertyFromModel(property) as KProperty1<E, Any>
        val entityValueList =
            value.map { entityMapper.valueFromModelProperty(property, value) }
        return database.getListByPropertyList(entityProperty, entityValueList, limit, skip)
            .mapNotNull { it.toModel() }
    }

    override suspend fun update(value: M): M? {
        val entityValue = entityMapper.fromModel(value) as E
        return database.update(value.id, entityValue)?.toModel()
    }
}