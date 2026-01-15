# ------------------
# Regras Mínimas do Kotlin
# ------------------

# Mantém os nomes dos Data Classes para que o método copy() funcione.
# A partir do Kotlin 1.6+ e R8 recente, isso pode não ser estritamente necessário,
# mas é bom como precaução.
-keepclassmembers class ** {
    public synthetic <methods>;
}

# Assume que métodos de verificação de nulo do Kotlin não têm efeitos colaterais
# para que o R8 possa removê-los e otimizar.
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
    public static void checkNotNull(java.lang.Object);
}

# Mantém classes sintéticas geradas para enums no Kotlin (necessário para o 'when' em enums)
# Embora o R8 melhore nisso, é uma regra comum de cautela.
-keepclassmembers class **$WhenMappings {
    <fields>;
}


# ------------------
# Regras de Log (Opcional, mas Recomendado)
# ------------------

# Remove todas as chamadas de Log (android.util.Log) na build de release.
# Isso reduz o tamanho e evita vazar informações de debug.
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** println(...);
}

# ------------------
# Regras para kotlinx.serialization (Se usado)
# ------------------

# Mantém todas as classes anotadas com @Serializable (do kotlinx.serialization)
-if @kotlinx.serialization.Serializable class * { !static *; }
-keep class %1 { *; }

# Regra adicional para manter os membros da classe, especialmente o Serializer gerado.
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# Manter o presentation sem ofuscação
-keep class com.kappstats.presentation.** { *; }
-keep interface com.kappstats.presentation.** { *; }
-keep enum com.kappstats.presentation.** { *; }
-keepclassmembers class com.kappstats.presentation.** { *; }