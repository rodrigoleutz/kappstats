package com.kappstats.presentation.util

fun apiLog(value: Any, name: String? = null) {
    println("START LOG${if(name != null) " -> $name" else ""}\n\n\n\n")
    println(value.toString()+"\n\n\n\n")
    println("END LOG${if(name != null) " -> $name" else ""}")
}