package com.kappstats.data.repository.app

import com.kappstats.data.entity.app.AppMonitorEntity
import com.kappstats.data.repository.Repository
import com.kappstats.model.app.AppMonitor

interface AppMonitorRepository: Repository<AppMonitor, AppMonitorEntity> {
}