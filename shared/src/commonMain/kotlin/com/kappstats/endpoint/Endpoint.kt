package com.kappstats.endpoint

interface Endpoint {
    val route: String
    val fullPath: String
}