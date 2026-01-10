package com.kappstats.data.entity.user

import com.kappstats.custom_object.app_date_time.AppDateTime
import com.kappstats.data.entity.EntityMapper
import com.kappstats.data.entity.EntityWithModel
import com.kappstats.model.user.AuthToken
import com.kappstats.model.user.TokenData
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlin.reflect.KProperty1


data class AuthTokenEntity(
    @field:BsonId override val _id: ObjectId = ObjectId(),
    val authId: String,
    val tokens: List<TokenData> = emptyList(),
    override val createdAt: AppDateTime = AppDateTime.now,
    override val updatedAt: List<AppDateTime> = emptyList()
) : EntityWithModel<AuthToken> {

    override val mapper: EntityMapper<AuthToken, AuthTokenEntity> = Companion

    companion object : EntityMapper<AuthToken, AuthTokenEntity> {
        override fun fromModel(
            value: AuthToken,
            vararg args: Any
        ): AuthTokenEntity? {
            return AuthTokenEntity(
                _id = if (value.id.isBlank()) ObjectId() else ObjectId(value.id),
                authId = value.authId,
                tokens = value.tokens,
                createdAt = value.createdAt,
                updatedAt = value.updatedAt
            )
        }

        override fun propertyFromModel(property: KProperty1<AuthToken, Any>): KProperty1<AuthTokenEntity, Any> {
            return when (property) {
                AuthToken::id -> AuthTokenEntity::_id
                AuthToken::authId -> AuthTokenEntity::authId
                AuthToken::tokens -> AuthTokenEntity::tokens
                AuthToken::createdAt -> AuthTokenEntity::createdAt
                AuthToken::updatedAt -> AuthTokenEntity::updatedAt
                else -> AuthTokenEntity::_id
            }
        }

        override fun valueFromModelProperty(
            property: KProperty1<AuthToken, Any>,
            value: Any
        ): Any? {
            return when (property) {
                AuthToken::id -> if (value is String) ObjectId(value) else null
                AuthToken::authId -> value as String
                AuthToken::tokens -> if (value is List<*>) value as List<TokenData> else null
                AuthToken::createdAt -> value as AppDateTime
                AuthToken::updatedAt -> if (value is List<*>) value as List<AppDateTime> else null
                else -> value as String
            }
        }
    }

    override fun toModel(): AuthToken? {
        return AuthToken(
            id = _id.toHexString(),
            authId = authId,
            tokens = tokens,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
