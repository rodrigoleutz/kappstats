package com.kappstats.data.repository.app

import com.kappstats.data.data_source.entity.app.AppMonitorEntity
import com.kappstats.data.data_source.remote.api.database.Database
import com.kappstats.data.data_source.remote.api.database.TableIndex
import com.kappstats.data.data_source.remote.api.database.mongo.MongoApi
import com.kappstats.data.data_source.remote.api.database.mongo.MongoDatabaseImpl
import com.kappstats.data.repository.GenericRepository
import com.kappstats.data.repository.GenericRepositoryWithModelImpl
import com.kappstats.model.app.AppMonitor
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList

class AppMonitorRepositoryImpl(
    private val mongoApi: MongoApi
) : AppMonitorRepository {

    private val database = MongoDatabaseImpl(
            api = mongoApi,
            clazz = AppMonitorEntity::class,
            TableIndex(
                AppMonitorEntity::owner
            ),
            TableIndex(
                AppMonitorEntity::hashId,
                isUnique = true
            ),
            TableIndex(
                AppMonitorEntity::members
            )
        )

    override val generic: GenericRepository<AppMonitor, AppMonitorEntity> =
        GenericRepositoryWithModelImpl(
            AppMonitorEntity,
            database = database
        )

    override suspend fun getListByProfileId(profileId: String): List<AppMonitor> {
        val filters = Filters.or(
            Filters.eq(AppMonitorEntity::owner.name, profileId),
            Filters.exists("${AppMonitorEntity::members.name}.$profileId")
        )
        return database.collection.find(filters).mapNotNull { it.toModel() }.toList()
    }
}