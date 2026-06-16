package com.example.sticbrain.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Clase encargada de guardar de forma cifrada la API key de Gemini.
 *
 * En lugar de guardar la clave directamente en Room, se almacena usando
 * EncryptedSharedPreferences. Android se encarga de proteger la información
 * mediante claves gestionadas por el sistema (Android Keystore).
 */
class SecureApiKeyStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        "stic_brain_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Guarda la API key de Gemini de forma cifrada.
     */
    fun saveGeminiApiKey(apiKey: String) {
        preferences.edit()
            .putString(KEY_GEMINI_API_KEY, apiKey)
            .apply()
    }

    /**
     * Obtiene la API key cifrada.
     */
    fun getGeminiApiKey(): String? {
        return preferences.getString(KEY_GEMINI_API_KEY, null)
    }

    /**
     * Elimina la API key guardada.
     */
    fun clearGeminiApiKey() {
        preferences.edit()
            .remove(KEY_GEMINI_API_KEY)
            .apply()
    }

    /**
     * Indica si existe una API key guardada.
     */
    fun hasGeminiApiKey(): Boolean {
        return !getGeminiApiKey().isNullOrBlank()
    }

    companion object {
        private const val KEY_GEMINI_API_KEY = "gemini_api_key"
    }
}
