package com.example.llmapp.data

    data class OpenRouterMessage(
        val role: String, // "user" or "assistant"
        val content: String
    )
