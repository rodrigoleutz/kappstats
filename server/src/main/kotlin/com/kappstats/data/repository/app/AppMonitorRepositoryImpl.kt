package com.kappstats.data.repository.app

import com.kappstats.data.entity.app.AppMonitorEntity
import com.kappstats.data.remote.api.database.Database
import com.kappstats.data.remote.api.database.TableIndex
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.data.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryWithModelImpl
import com.kappstats.model.app.AppMonitor

class AppMonitorRepositoryImpl(
    private val mongoApi: MongoApi
) : AppMonitorRepository {

    private val database: Database<AppMonitorEntity> =
        MongoDatabaseImpl(
            api = mongoApi,
            clazz = AppMonitorEntity::class,
            TableIndex(
                AppMonitorEntity::owner
            )
        )

    override val generic: GenericRepository<AppMonitor, AppMonitorEntity> =
        GenericRepositoryWithModelImpl(
            AppMonitorEntity,
            database = database
        )
}