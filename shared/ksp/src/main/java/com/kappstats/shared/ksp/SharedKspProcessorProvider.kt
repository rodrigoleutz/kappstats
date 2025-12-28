package com.kappstats.shared.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class SharedKspProcessorProvider: SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val gitBranch = environment.options["GIT_BRANCH"]
        return ConfigProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            gitBranch = gitBranch
        )
    }
}