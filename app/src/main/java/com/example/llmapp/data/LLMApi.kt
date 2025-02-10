package com.example.llmapp.data

    import okhttp3.MultipartBody
    import okhttp3.RequestBody
    import retrofit2.Response
    import retrofit2.http.Body
    import retrofit2.http.Header
    import retrofit2.http.Multipart
    import retrofit2.http.POST
    import retrofit2.http.Part

    interface LLMApi {
        //OpenAI
        @POST("chat/completions")
        suspend fun getOpenAICompletion(
            @Header("Authorization") authorization: String,
            @Body request: OpenAIRequest
        ): Response<OpenAIResponse>

        //OpenRouter
        @POST("chat/completions")
        suspend fun getOpenRouterCompletion(
            @Header("Authorization") authorization: String,
            @Header("HTTP-Referer") httpReferer: String,
            @Header("X-Title") xTitle: String,
            @Body request: OpenRouterRequest
        ): Response<OpenRouterResponse>

        // Generic Multimodal (Example - Adapt based on actual API)
        @Multipart
        @POST("your/multimodal/endpoint") // Replace with the actual endpoint
        suspend fun getMultimodalCompletion(
            @Header("Authorization") authorization: String,
            @Part("prompt") prompt: RequestBody,  // Text prompt as RequestBody
            @Part image: MultipartBody.Part      // Image part
        ): Response<MultimodalResponse> // Define a MultimodalResponse data class
    }
