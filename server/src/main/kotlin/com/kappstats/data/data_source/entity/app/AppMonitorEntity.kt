package com.kappstats.data.data_source.entity.app

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.data_source.entity.EntityMapper
import com.kappstats.data.data_source.entity.EntityWithModel
import com.kappstats.model.app.AppMemberType
import com.kappstats.model.app.AppMonitor
import com.kappstats.util.IdGenerator
import org.bson.types.ObjectId
import kotlin.reflect.KProperty1

data class AppMonitorEntity(
    override val _id: ObjectId = ObjectId(),
    val owner: String,
    val name: String,
    val description: String,
    val hashId: String = IdGenerator.generateHashingId,
    val members: Map<String, AppMemberType> = emptyMap(),
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
) : EntityWithModel<AppMonitor> {

    override val mapper: EntityMapper<AppMonitor, AppMonitorEntity> = Companion

    companion object : EntityMapper<AppMonitor, AppMonitorEntity> {
        override fun fromModel(
            value: AppMonitor,
            vararg args: Any
        ): AppMonitorEntity? {
            return AppMonitorEntity(
                _id = if (value.id.isBlank()) ObjectId() else ObjectId(value.id),
                owner = value.owner,
                name = value.name,
                description = value.description,
                hashId = value.hashId,
                members = value.members,
                createdAt = value.createdAt,
                updatedAt = value.updatedAt
            )
        }

        override fun propertyFromModel(property: KProperty1<AppMonitor, Any>): KProperty1<AppMonitorEntity, Any> {
            return when (property) {
                AppMonitor::id -> AppMonitorEntity::_id
                AppMonitor::owner -> AppMonitorEntity::owner
                AppMonitor::name -> AppMonitorEntity::name
                AppMonitor::description -> AppMonitorEntity::description
                AppMonitor::hashId -> AppMonitorEntity::hashId
                AppMonitor::members -> AppMonitorEntity::members
                AppMonitor::createdAt -> AppMonitorEntity::createdAt
                AppMonitor::updatedAt -> AppMonitorEntity::updatedAt
                else -> AppMonitorEntity::_id
            }
        }

        override fun valueFromModelProperty(
            property: KProperty1<AppMonitor, Any>,
            value: Any
        ): Any? {
            return when (property) {
                AppMonitor::id -> if (value is String) ObjectId(value) else null
                AppMonitor::owner -> value as? String
                AppMonitor::name -> value as? String
                AppMonitor::description -> value as? String
                AppMonitor::hashId -> value as? String
                AppMonitor::members -> if(value is Map<*,*>) value as Map<String, AppMemberType> else null
                AppMonitor::createdAt -> value as AppDateTime
                AppMonitor::updatedAt -> if (value is List<*>) value as List<AppDateTime> else null
                else -> value as? String
            }
        }
    }

    override fun toModel(): AppMonitor? {
        return AppMonitor(
            id = _id.toHexString(),
            owner = owner,
            name = name,
            description = description,
            hashId = hashId,
            members = members,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
