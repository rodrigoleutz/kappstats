package com.kappstats.data.repository

import com.kappstats.contracts.Model
import com.kappstats.data.entity.Entity

interface Repository<M: Model, E: Entity<M>> {
    val generic: GenericRepository<M, E>
}