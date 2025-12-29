package com.kappstats.data.entity.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.username.Username
import com.kappstats.data.entity.Entity
import com.kappstats.data.entity.EntityMapper
import com.kappstats.model.user.Profile
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.types.ObjectId
import kotlin.reflect.KProperty1

data class ProfileEntity(
    @field:BsonId override val _id: ObjectId,
    val name: String,
    val username: String,
    val bio: String,
    override val createdAt: AppDateTime,
    override val updatedAt: List<AppDateTime>,
): Entity<Profile> {

    @get:BsonIgnore
    override val mapper: EntityMapper<Profile, ProfileEntity> = Companion

    companion object: EntityMapper<Profile, ProfileEntity> {
        override fun fromModel(
            value: Profile,
            vararg args: Any
        ): ProfileEntity? {
            return ProfileEntity(
                _id = if(value.id.isBlank()) ObjectId() else ObjectId(value.id),
                name = value.name,
                username = value.username.asString,
                bio = value.bio,
                createdAt = value.createdAt,
                updatedAt = value.updatedAt
            )
        }

        override fun propertyFromModel(property: KProperty1<Profile, Any>): KProperty1<ProfileEntity, Any> =
            when(property) {
                Profile::id -> ProfileEntity::_id
                Profile::name -> ProfileEntity::name
                Profile::username -> ProfileEntity::username
                Profile::bio -> ProfileEntity::bio
                Profile::createdAt -> ProfileEntity::createdAt
                Profile::updatedAt -> ProfileEntity::updatedAt
                else -> ProfileEntity::_id
            }

        override fun propertyWithValueFromModel(
            property: KProperty1<Profile, Any>,
            value: Any
        ): Pair<KProperty1<ProfileEntity, Any>, Any?> {
            val responseProperty = propertyFromModel(property)
            val responseValue = valueFromModelProperty(property, value)
            return responseProperty to responseValue
        }

        override fun valueFromModelProperty(
            property: KProperty1<Profile, Any>,
            value: Any
        ): Any? {
            return when(property) {
                Profile::id -> if(value is String) ObjectId(value) else null
                Profile::name -> value
                Profile::username -> if(value is Username) value.asString else null
                Profile::bio -> value
                Profile::createdAt -> value
                Profile::updatedAt -> value
                else -> null
            }
        }
    }

    override fun toModel(): Profile? {
        val usernameToProfile = try {
            Username(username)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return Profile(
            id = _id.toHexString(),
            name = name,
            username = usernameToProfile,
            bio = bio,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
