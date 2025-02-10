package com.example.llmapp.data

    data class OpenRouterResponse(
        val choices: List<Choice>
        // Include other fields like 'model', 'usage', etc. if needed
    ) {
        data class Choice(
            val message: OpenRouterMessageResponse
            // You might have 'index', 'finish_reason', etc. here too
        )
    }
    data class OpenRouterMessageResponse(
      val role: String,
      val content: String
    )
