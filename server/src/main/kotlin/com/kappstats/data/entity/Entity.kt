package com.kappstats.data.entity

import com.kappstats.contracts.Model
import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.entity.user.AuthEntity
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import kotlin.reflect.KProperty1

interface Entity<M : @Serializable Model> {
    val _id: ObjectId
    val createdAt: AppDateTime
    val updatedAt: List<AppDateTime>
    val mapper: EntityMapper<M, *>
    fun toModel(): M?
}

interface EntityMapper<M : Model, E : Entity<M>> {
    fun fromModel(value: M, vararg args: Any = arrayOf()): E?
    fun propertyFromModel(property: KProperty1<M, Any>): KProperty1<E, Any>
    fun propertyWithValueFromModel(
        property: KProperty1<M, Any>,
        value: Any
    ): Pair<KProperty1<E, Any>, Any?> {
        val responseValue = valueFromModelProperty(property, value)
        val responseProperty = propertyFromModel(property)
        return responseProperty to responseValue
    }
    fun valueFromModelProperty(
        property: KProperty1<M, Any>,
        value: Any
    ): Any?
}