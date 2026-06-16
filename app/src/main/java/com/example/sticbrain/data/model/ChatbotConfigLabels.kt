package com.example.sticbrain.data.model

/**
 * Extensiones para obtener textos legibles para los enums de configuración.
 */

fun ChatbotResponseStyle.toReadableText(): String {
    return when (this) {
        ChatbotResponseStyle.DIAGNOSTICO_BREVE -> "Diagnóstico breve"
        ChatbotResponseStyle.PROCEDIMIENTO_PASO_A_PASO -> "Procedimiento paso a paso"
        ChatbotResponseStyle.RESUMEN_EJECUTIVO -> "Resumen ejecutivo"
    }
}

fun ChatbotDetailLevel.toReadableText(): String {
    return when (this) {
        ChatbotDetailLevel.BAJO -> "Bajo"
        ChatbotDetailLevel.MEDIO -> "Medio"
        ChatbotDetailLevel.ALTO -> "Alto"
    }
}
