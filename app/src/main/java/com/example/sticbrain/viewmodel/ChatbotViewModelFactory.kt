package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sticbrain.data.repository.ChatbotConfigRepository
import com.example.sticbrain.data.repository.ChatMessageRepository
import com.example.sticbrain.data.repository.IncidenciaRepository

/**
 * Factoría para crear una instancia de ChatbotViewModel con sus dependencias.
 */
class ChatbotViewModelFactory(
    private val incidenciaRepository: IncidenciaRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatbotConfigRepository: ChatbotConfigRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatbotViewModel::class.java)) {
            return ChatbotViewModel(
                incidenciaRepository = incidenciaRepository,
                chatMessageRepository = chatMessageRepository,
                chatbotConfigRepository = chatbotConfigRepository
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
