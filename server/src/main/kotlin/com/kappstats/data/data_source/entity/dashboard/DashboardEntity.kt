package com.kappstats.data.data_source.entity.dashboard

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.data_source.entity.Entity
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class DashboardEntity(
    @field:BsonId override val _id: ObjectId = ObjectId(),
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
) : Entity {

}
