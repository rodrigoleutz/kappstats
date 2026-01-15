package com.kappstats.composeapp.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.kappstats.dto.web_socket.WsAction
import java.io.OutputStream

class ComposeAppKspProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {

    private var isInvoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if(isInvoked) return emptyList()
        isInvoked = true
        generateWsActions(resolver)
        return emptyList()
    }

    fun generateWsActions(resolver: Resolver): OutputStream? {
        return try {
            val targetPackage = "com.kappstats.domain.web_socket.generated"
            val classes = resolver.getAllAnnotatedClass(WsAction::class.qualifiedName!!)
            println("Classes: $classes")
            if(classes.isEmpty()) return null
            val interfaceName = "WebSocketContract"
            val classesFiles = classes.mapNotNull { it.containingFile }.toTypedArray()
            val fileName = "${WsAction::class.simpleName}sGenerated"
            val outputStream = environment.codeGenerator.createNewFile(
                dependencies = Dependencies(aggregating = true, *classesFiles),
                packageName = targetPackage,
                fileName = fileName
            )
            outputStream.bufferedWriter().use { writer ->
                writer.write("package $targetPackage\n\n")
                writer.write("import com.kappstats.domain.web_socket.contract.WebSocketContract\n\n")
                writer.write("class $fileName<T, R> {\n")
                writer.write("    val list: List<$interfaceName<T, R>> = listOf(\n")
                classes.forEachIndexed { index, clazz ->
                    val qualifiedName = clazz.qualifiedName?.asString()
                    val separator = if(index != classes.lastIndex) "," else ""
                    writer.write("        $qualifiedName$separator\n")
                }
                writer.write("    ) as List<WebSocketContract<T, R>>\n")
                writer.write("}\n")
            }
            outputStream
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun Resolver.getAllAnnotatedClass(annotation: String): List<KSClassDeclaration> {
        return this.getSymbolsWithAnnotation(annotation).filterIsInstance<KSClassDeclaration>().toList()
    }
}