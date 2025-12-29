package com.kappstats.data.repository.user

import com.kappstats.data.entity.user.AuthEntity
import com.kappstats.data.remote.api.database.TableIndex
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryImpl
import com.kappstats.model.user.Auth

class AuthRepositoryImpl(mongoApi: MongoApi) : AuthRepository {

    @Deprecated("Do not use generic in this repository")
    override val generic: GenericRepository<Auth, AuthEntity> =
        GenericRepositoryImpl(
            entityMapper = AuthEntity,
            database = MongoDatabaseImpl(
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
        )



}