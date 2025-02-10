package com.example.llmapp.data

    data class OpenRouterRequest(
        val model: String,
        val messages: List<OpenRouterMessage>,
        val temperature: Double = 0.7,
        val max_tokens: Int? = null,
        val top_p: Double? = null,
        val frequency_penalty: Double? = null,
        val presence_penalty: Double? = null
        // Add other parameters as needed, refer to OpenRouter API docs
    )
