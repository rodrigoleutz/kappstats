package com.kappstats.data.service

interface Service<T> {
    suspend fun <R> run(value: R): T?
}