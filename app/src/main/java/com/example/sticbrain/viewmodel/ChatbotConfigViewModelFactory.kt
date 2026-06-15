package com.example.sticbrain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sticbrain.data.repository.ChatbotConfigRepository

/**
 * Factory para crear ChatbotConfigViewModel con su repositorio.
 */
class ChatbotConfigViewModelFactory(
    private val chatbotConfigRepository: ChatbotConfigRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatbotConfigViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatbotConfigViewModel(chatbotConfigRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
