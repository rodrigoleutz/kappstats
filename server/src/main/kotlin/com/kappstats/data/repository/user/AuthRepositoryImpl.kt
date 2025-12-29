package com.kappstats.data.repository.user

import com.kappstats.custom_object.email.Email
import com.kappstats.data.entity.user.AuthEntity
import com.kappstats.data.remote.api.database.TableIndex
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryImpl
import com.kappstats.domain.core.security.hashing.SaltedHash
import com.kappstats.model.user.Auth

class AuthRepositoryImpl(
    mongoApi: MongoApi
) : AuthRepository {

    private val database = MongoDatabaseImpl(
        api = mongoApi,
        clazz = AuthEntity::class,
        indexes = arrayOf(
            TableIndex(
                property = AuthEntity::email,
                isUnique = true
            ),
            TableIndex(
                property = AuthEntity::profileId,
                isUnique = true
            )
        )
    )

    @Deprecated("Do not use generic in this repository")
    override val generic: GenericRepository<Auth, AuthEntity> =
        GenericRepositoryImpl(
            entityMapper = AuthEntity,
            database = database
        )

    override suspend fun add(
        value: Auth,
        saltedHash: SaltedHash
    ): Auth? {
        return try {
            AuthEntity.fromModel(value, saltedHash.salt, saltedHash.hash)?.let { entity ->
                database.add(entity)?.toModel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteById(value: String): Boolean {
        return try {
            database.delete(value)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getAuthAndSaltedHash(email: Email): Pair<Auth, SaltedHash>? {
        return try {
            val entity = database.getByProperty(AuthEntity::email, email.asString)
                ?: return null
            val auth = entity.toModel() ?: return null
            val saltedHash = SaltedHash(salt = entity.salt, hash = entity.hash)
            auth to saltedHash
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}