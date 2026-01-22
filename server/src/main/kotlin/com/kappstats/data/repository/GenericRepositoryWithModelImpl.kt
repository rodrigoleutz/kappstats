package com.kappstats.data.repository

import com.kappstats.contracts.Model
import com.kappstats.data.data_source.entity.EntityWithModel
import com.kappstats.data.data_source.entity.EntityMapper
import com.kappstats.data.data_source.remote.api.database.Database
import kotlin.reflect.KProperty1

class GenericRepositoryWithModelImpl<M : Model, E : EntityWithModel<M>>(
    private val entityMapper: EntityMapper<M, E>,
    override val database: Database<E>
) : GenericRepository<M, E> {
    private val generic = GenericRepositoryImpl<E>(database)

    override suspend fun add(value: M): M? {
        val entityValue = entityMapper.fromModel(value) as E
        return database.add(entityValue)?.toModel()
    }

    override suspend fun delete(value: M): Boolean =
        entityMapper.fromModel(value)?.let { generic.delete(it) } ?: false

    override suspend fun deleteById(value: String): Boolean = generic.deleteById(value)
    override suspend fun getById(value: String): M? = generic.getById(value)?.toModel()

    override suspend fun getListByProperty(
        property: KProperty1<M, Any>,
        value: Any,
        limit: Int,
        skip: Int
    ): List<M> {
        val entityValues = entityMapper.propertyWithValueFromModel(property, value)
        if(entityValues.second == null) return emptyList()
        return generic.getListByProperty(entityValues.first, entityValues.second!!, limit, skip)
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
            value.mapNotNull { entityMapper.valueFromModelProperty(property, value) }
        return generic.getListByPropertyList(entityProperty, entityValueList, limit, skip)
            .mapNotNull { it.toModel() }
    }

    override suspend fun update(value: M): M? {
        val entityValue = entityMapper.fromModel(value) as E
        return generic.update(entityValue)?.toModel()
    }
}