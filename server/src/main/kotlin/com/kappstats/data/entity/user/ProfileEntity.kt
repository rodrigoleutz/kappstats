package com.kappstats.data.entity.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.username.Username
import com.kappstats.data.entity.Entity
import com.kappstats.data.entity.EntityMapper
import com.kappstats.model.user.Profile
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class ProfileEntity(
    @field:BsonId override val _id: ObjectId,
    val name: String,
    val username: String,
    val bio: String,
    override val createdAt: AppDateTime,
    override val updatedAt: List<AppDateTime>,
    @field:Transient override val mapper: EntityMapper<Profile, ProfileEntity> = Companion
): Entity<Profile> {

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
