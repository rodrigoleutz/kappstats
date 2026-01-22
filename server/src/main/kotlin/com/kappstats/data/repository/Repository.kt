package com.kappstats.data.repository

import com.kappstats.data.data_source.entity.Entity

interface Repository<T, D: Entity> {
    val generic: GenericRepository<T, D>
}