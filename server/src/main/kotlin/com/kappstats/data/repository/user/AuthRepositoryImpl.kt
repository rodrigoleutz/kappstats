package com.kappstats.data.repository.user

import com.kappstats.custom_object.email.Email
import com.kappstats.data.entity.user.AuthEntity
import com.kappstats.data.remote.api.database.TableIndex
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryWithModelImpl
import com.kappstats.domain.core.security.hashing.SaltedHash
import com.kappstats.model.user.Auth
import org.bson.types.ObjectId

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
        GenericRepositoryWithModelImpl(
            entityMapper = AuthEntity,
            database = database
        )

    override suspend fun add(
        value: Auth,
        saltedHash: SaltedHash
    ): Auth? = AuthEntity.fromModel(value, saltedHash.salt, saltedHash.hash)?.let { entity ->
        database.add(entity)?.toModel()
    }

    override suspend fun deleteById(value: String): Boolean {
        return database.delete(value)
    }

    override suspend fun getAuthAndSaltedHashByEmail(email: Email): Pair<Auth, SaltedHash>? {
        val entity = database.getByProperty(AuthEntity::email, email.asString)
            ?: return null
        val auth = entity.toModel() ?: return null
        val saltedHash = SaltedHash(salt = entity.salt, hash = entity.hash)
        return auth to saltedHash
    }

    override suspend fun getAuthAndSaltedHashById(id: String): Pair<Auth, SaltedHash>? {
        val entity = database.getByProperty(AuthEntity::_id, ObjectId(id))
            ?: return null
        val auth = entity.toModel() ?: return null
        val saltedHash = SaltedHash(salt = entity.salt, hash = entity.hash)
        return auth to saltedHash
    }

    override suspend fun update(
        value: Auth,
        saltedHash: SaltedHash?
    ): Auth? {
        val newSaltedHash = saltedHash
            ?: getAuthAndSaltedHashById(value.id)?.second ?: return null
        return AuthEntity.fromModel(value, newSaltedHash.salt, newSaltedHash.hash)?.let { entity ->
            database.update(value.id, entity)?.toModel()
        }
    }
}