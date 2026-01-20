package com.kappstats.data.entity.app

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.entity.EntityMapper
import com.kappstats.data.entity.EntityWithModel
import com.kappstats.model.app.AppMonitor
import org.bson.types.ObjectId
import kotlin.reflect.KProperty1

data class AppMonitorEntity(
    override val _id: ObjectId = ObjectId(),
    val owner: String,
    val name: String,
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
): EntityWithModel<AppMonitor> {

    override val mapper: EntityMapper<AppMonitor, AppMonitorEntity> = Companion

    companion object: EntityMapper<AppMonitor, AppMonitorEntity> {
        override fun fromModel(
            value: AppMonitor,
            vararg args: Any
        ): AppMonitorEntity? {
            return AppMonitorEntity(
                _id = if(value.id.isBlank()) ObjectId() else ObjectId(value.id),
                owner = value.owner,
                name = value.name,
                createdAt = value.createdAt,
                updatedAt = value.updatedAt
            )
        }

        override fun propertyFromModel(property: KProperty1<AppMonitor, Any>): KProperty1<AppMonitorEntity, Any> {
            return when(property) {
                AppMonitor::id -> AppMonitorEntity::_id
                AppMonitor::owner -> AppMonitorEntity::owner
                AppMonitor::name -> AppMonitorEntity::name
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
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
