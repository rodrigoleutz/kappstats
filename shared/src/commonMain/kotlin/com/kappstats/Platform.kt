package com.kappstats

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform