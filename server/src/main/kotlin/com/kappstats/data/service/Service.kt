package com.kappstats.data.service

interface Service<R, T> {
    suspend fun exec(value: R): T?
}