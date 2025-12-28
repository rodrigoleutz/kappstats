package com.kappstats.shared.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

class ConfigProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val gitBranch: String?
) : SymbolProcessor {

    private var isInvoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isInvoked) return emptyList()
        isInvoked = true
        val branch = gitBranch ?: "dev"
        generateBuildConfigClass(
            branch
        )
        return emptyList()
    }

    private fun generateBuildConfigClass(
        branch: String
    ) {
        val configClass = TypeSpec.objectBuilder("ProjectConfig")
            .addKdoc("Classe gerada automaticamente pelo KSP. Não edite manualmente.")
            .addProperty(
                PropertySpec.builder("GIT_BRANCH", String::class)
                    .initializer("%S", branch)
                    .build()
            )
            .build()
        val packageName = "com.kappstats.constants.config"
        val fileName = "ProjectConfig"
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = fileName
        ).addType(configClass).build()
        val kspMetadataDir = "build/generated/ksp/metadata/commonMain/kotlin/"
        val packagePath = packageName.replace('.', '/')
        val moduleRoot = File("shared")
        val fileToDelete = File(moduleRoot, kspMetadataDir + packagePath + "/" + fileName + ".kt")
        if (fileToDelete.exists()) {
            val deleted = fileToDelete.delete()
            if (deleted) {
                logger.logging("Arquivo antigo ProjectConfig.kt excluído com sucesso.")
            } else {
                logger.error("Falha ao excluir o arquivo antigo. O conflito persistirá.", null)
            }
        }
        val dependencies = Dependencies(aggregating = false, sources = emptyArray())
        val outputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = packageName,
            fileName = fileName
        )
        outputStream.use { stream ->
            val writer = stream.writer()
            fileSpec.writeTo(writer)
            writer.flush()
        }
    }
}