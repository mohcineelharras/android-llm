package com.example.llmapp.data

    data class OpenAIRequest(
        val model: String,
        val messages: List<OpenAIMessage>,
        val temperature: Double = 0.7,
        val max_tokens: Int = 800
    )
