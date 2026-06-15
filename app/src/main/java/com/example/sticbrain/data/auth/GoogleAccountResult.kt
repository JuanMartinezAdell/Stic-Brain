package com.example.sticbrain.data.auth

/**
 * Resultado de la confirmación de cuenta con Google.
 */
data class GoogleAccountResult(
    val email: String,
    val displayName: String?
)
