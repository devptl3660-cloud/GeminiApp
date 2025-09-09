package com.example.geminiapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminiapp.data.ChatMessage
import com.example.geminiapp.data.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    val messages = mutableStateListOf<ChatMessage>()
    var inputText by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    private val prefs: SharedPreferences =
        application.getSharedPreferences("gemini_prefs", Context.MODE_PRIVATE)
    var isDarkTheme by mutableStateOf(prefs.getBoolean("dark", false))
        private set

    private val repository = RemoteRepository.create()

    init {
        messages.add(ChatMessage("system", "Welcome! Ask anything."))
    }

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        prefs.edit().putBoolean("dark", isDarkTheme).apply()
    }

    fun sendMessage() {
        val prompt = inputText.trim()
        if (prompt.isEmpty()) return
        messages.add(ChatMessage("user", prompt))
        inputText = ""
        callGemini(prompt)
    }

    private fun callGemini(prompt: String) {
        isLoading = true
        viewModelScope.launch {
            val responseText = withContext(Dispatchers.IO) {
                try {
                    repository.generateText(prompt)
                } catch (e: Exception) {
                    "Error: ${e.localizedMessage}"
                }
            }
            messages.add(ChatMessage("assistant", responseText ?: "No response"))
            isLoading = false
        }
    }

    fun onSpeechResult(text: String) {
        inputText += " $text"
    }
}
