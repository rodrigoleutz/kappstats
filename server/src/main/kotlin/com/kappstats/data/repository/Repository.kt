package com.kappstats.data.repository

interface Repository<T, D> {
    val generic: GenericRepository<T, D>
}