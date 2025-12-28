package com.kappstats.data.entity.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.custom_object.email.Email
import com.kappstats.data.entity.Entity
import com.kappstats.data.entity.EntityMapper
import com.kappstats.model.user.Auth
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class AuthEntity(
    @field:BsonId override val _id: ObjectId,
    val email: String,
    val profileId: String,
    val salt: String,
    val hash: String,
    override val createdAt: AppDateTime,
    override val updatedAt: List<AppDateTime>,
    @field:Transient override val mapper: EntityMapper<Auth, AuthEntity> = Companion,
) : Entity<Auth> {
    companion object : EntityMapper<Auth, AuthEntity> {
        override fun fromModel(value: Auth, vararg args: Any): AuthEntity? {
            if (args.size != 2 || args.any { arg -> arg !is String }) return null
            val salt = args[0] as String
            val hash = args[1] as String
            return AuthEntity(
                _id = if (value.id.isBlank()) ObjectId() else ObjectId(value.id),
                email = value.email.asString,
                profileId = value.profileId,
                salt = salt,
                hash = hash,
                createdAt = value.createdAt,
                updatedAt = value.updatedAt
            )
        }
    }

    override fun toModel(): Auth? {
        val email = try {
            Email(email)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return Auth(
            id = _id.toHexString(),
            email = email,
            profileId = profileId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
