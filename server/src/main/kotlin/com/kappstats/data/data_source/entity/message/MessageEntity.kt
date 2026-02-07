package com.kappstats.data.data_source.entity.message

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.data_source.entity.EntityMapper
import com.kappstats.data.data_source.entity.EntityWithModel
import com.kappstats.model.message.Message
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

//data class MessageEntity(
//    @field:BsonId override val _id: ObjectId = ObjectId(),
//
//    override val createdAt: AppDateTime = AppDateTime.now,
//    override val updatedAt: List<AppDateTime> = emptyList()
//): EntityWithModel<Message> {
//
//    companion object: EntityMapper<Message, MessageEntity> {
//
//
//    }
//}