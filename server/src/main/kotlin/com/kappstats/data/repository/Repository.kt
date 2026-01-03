package com.kappstats.data.repository

import com.kappstats.data.entity.Entity

interface Repository<T, D: Entity> {
    val generic: GenericRepository<T, D>
}