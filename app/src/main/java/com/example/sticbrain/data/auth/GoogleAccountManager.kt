package com.example.sticbrain.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Gestor para la confirmación de cuenta mediante Google Identity Services / Credential Manager.
 *
 * Se utiliza para verificar la identidad del usuario y obtener su correo electrónico
 * de forma segura antes de habilitar funciones de IA externa.
 */
class GoogleAccountManager(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    /**
     * Inicia el flujo de selección de cuenta de Google.
     *
     * Nota: En un entorno real, 'serverClientId' debería ser el ID de cliente Web
     * configurado en Google Cloud Console.
     */
    suspend fun confirmarCuentaGoogle(serverClientId: String): GoogleAccountResult? = withContext(Dispatchers.IO) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Permitir elegir cualquier cuenta del dispositivo
                .setServerClientId(serverClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            if (credential is GoogleIdTokenCredential) {
                return@withContext GoogleAccountResult(
                    email = credential.id, // El ID en este caso es el email
                    displayName = credential.displayName
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}
