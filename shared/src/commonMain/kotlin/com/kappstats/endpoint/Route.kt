package com.kappstats.endpoint


open class Route(val parent: Endpoint? = null, val path: String) : Endpoint {
    override val route: String by lazy { "${parent?.route ?: ""}$path" }
    override val fullPath: String by lazy {
        val path = route.replace("//", "")
        "/" + path.substringAfter("/")
    }
}