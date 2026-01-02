package com.kappstats.data.repository

import com.kappstats.contracts.Model
import com.kappstats.data.entity.EntityWithModel

interface Repository<M: Model, E: EntityWithModel<M>> {
    val generic: GenericRepository<M, E>
}