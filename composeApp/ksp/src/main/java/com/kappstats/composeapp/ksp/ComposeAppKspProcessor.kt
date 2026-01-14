package com.kappstats.composeapp.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class ComposeAppKspProcessor: SymbolProcessor {

    private var isInvoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if(isInvoked) return emptyList()
        isInvoked = true
        return emptyList()
    }
}