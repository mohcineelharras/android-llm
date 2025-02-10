package com.example.llmapp.data

    data class Message(
        val text: String,
        val isUser: Boolean,
        val imageUrl: String? = null // Base64 encoded image
    )
