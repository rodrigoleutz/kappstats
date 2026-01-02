package com.kappstats.data.repository.user

import com.kappstats.data.entity.user.ProfileEntity
import com.kappstats.data.remote.api.database.TableIndex
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryWithModelImpl
import com.kappstats.model.user.Profile

class ProfileRepositoryImpl(mongoApi: MongoApi): ProfileRepository {

    override val generic: GenericRepository<Profile, ProfileEntity> =
        GenericRepositoryWithModelImpl(
            entityMapper = ProfileEntity,
            database = MongoDatabaseImpl(
                api = mongoApi,
                clazz = ProfileEntity::class,
                indexes = arrayOf(
                    TableIndex(
                        property = ProfileEntity::name
                    ),
                    TableIndex(
                        property = ProfileEntity::username,
                        isUnique = true
                    )
                )
            )
        )
}