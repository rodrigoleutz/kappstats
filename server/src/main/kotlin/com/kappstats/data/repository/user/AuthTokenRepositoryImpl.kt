package com.kappstats.data.repository.user

import com.kappstats.data.entity.user.AuthTokenEntity
import com.kappstats.data.remote.api.database.Database
import com.kappstats.data.remote.api.database.TableIndex
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryWithModelImpl
import com.kappstats.model.user.AuthToken

class AuthTokenRepositoryImpl(
    private val api: MongoApi
): AuthTokenRepository {

    val database: Database<AuthTokenEntity> = MongoDatabaseImpl(
        api,
        AuthTokenEntity::class,
        TableIndex(AuthTokenEntity::authId, isUnique = true)
    )

    override val generic: GenericRepository<AuthToken, AuthTokenEntity> =
        GenericRepositoryWithModelImpl(AuthTokenEntity, database)
}