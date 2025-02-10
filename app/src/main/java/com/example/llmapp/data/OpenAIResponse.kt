package com.example.llmapp.data

    data class OpenAIResponse(
        val choices: List<Choice>
    ) {
        data class Choice(
            val message: OpenAIMessageResponse
        )
    }

    data class OpenAIMessageResponse(
      val role: String,
      val content: String
    )
