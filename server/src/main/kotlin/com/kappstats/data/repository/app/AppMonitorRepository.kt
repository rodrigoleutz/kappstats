package com.kappstats.data.repository.app

import com.kappstats.data.data_source.entity.app.AppMonitorEntity
import com.kappstats.data.repository.Repository
import com.kappstats.model.app.AppMonitor

interface AppMonitorRepository: Repository<AppMonitor, AppMonitorEntity> {

    suspend fun getListByProfileId(profileId: String): List<AppMonitor>
}