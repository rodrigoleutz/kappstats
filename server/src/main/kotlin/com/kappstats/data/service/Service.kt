package com.kappstats.data.service

interface Service<R, T> {
    suspend fun run(value: R): T?
}