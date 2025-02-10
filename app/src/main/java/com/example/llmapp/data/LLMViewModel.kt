package com.example.llmapp

    import android.graphics.Bitmap
    import android.util.Base64
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.llmapp.data.LLMApi
    import com.example.llmapp.data.Message
    import com.example.llmapp.data.OpenAIRequest
    import com.example.llmapp.data.OpenRouterRequest
    import kotlinx.coroutines.launch
    import java.io.ByteArrayOutputStream
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.toRequestBody
    import com.example.llmapp.data.OpenAIMessage
    import com.example.llmapp.data.OpenRouterMessage

    class LLMViewModel : ViewModel() {

        var messages = mutableStateOf(listOf<Message>())
            private set
        var inputText = mutableStateOf("")
            private set
        var selectedImage = mutableStateOf<Bitmap?>(null)
            private set

        private val retrofit = RetrofitClient.instance
        private val api = retrofit.create(LLMApi::class.java)

      fun onInputTextChanged(text: String) {
        inputText.value = text
      }

      fun onImageSelected(bitmap: Bitmap?) {
        selectedImage.value = bitmap
      }

      fun sendMessage(apiKey: String, apiType: APIType) {
          val userMessage = Message(text = inputText.value, isUser = true, imageUrl = selectedImage.value?.let { encodeImageToBase64(it) })
          messages.value = messages.value + userMessage
          inputText.value = "" // Clear input
          selectedImage.value = null // Clear image

          viewModelScope.launch {
              try {
                  val response = when (apiType) {
                      APIType.OPENAI -> {
                        val openAIMessages = messages.value.map {
                          OpenAIMessage(
                            role = if (it.isUser) "user" else "assistant",
                            content = buildOpenAIMessageContent(it)
                          )
                        }
                        val request = OpenAIRequest(
                          model = "gpt-3.5-turbo", // Or any other model you want to use
                          messages = openAIMessages
                        )
                        api.getOpenAICompletion("Bearer $apiKey", request)
                      }
                      APIType.OPENROUTER -> {
                        val openRouterMessages = messages.value.map {
                          OpenRouterMessage(
                            role = if (it.isUser) "user" else "assistant",
                            content = buildOpenRouterMessageContent(it)
                          )
                        }

                        val request = OpenRouterRequest(
                          model = "mistralai/mistral-medium", // Example model, change as needed.
                          messages = openRouterMessages
                        )
                        api.getOpenRouterCompletion(
                          "Bearer $apiKey",
                          "http://localhost", // Replace with your app's URL or identifier
                          "Your App Name", // Replace with your app's name
                          request
                        )
                      }
                      APIType.MULTIMODAL -> {
                        // Assuming LLaVA or similar through a custom API
                        val promptRequestBody = inputText.value.toRequestBody("text/plain".toMediaTypeOrNull())
                        val imagePart = selectedImage.value?.let {
                            val base64Image = encodeImageToBase64(it)
                            val requestBody = base64Image.toRequestBody("image/*".toMediaTypeOrNull()) // or "application/octet-stream"
                            MultipartBody.Part.createFormData("image", "image.jpg", requestBody) // "image" is the parameter name the server expects
                        }

                        if (imagePart != null) {
                            api.getMultimodalCompletion("Bearer $apiKey", promptRequestBody, imagePart)
                        } else {
                            // Handle the case where no image is selected, maybe send only text
                            null // Or throw an exception, or send a text-only request
                        }
                      }
                  }

                  if (response != null &amp;&amp; response.isSuccessful) {
                    val assistantResponse = when (apiType) {
                        APIType.OPENAI -> response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                        APIType.OPENROUTER -> response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                        APIType.MULTIMODAL -> response.body()?.text ?: "" // Assuming a 'text' field in your MultimodalResponse
                    }

                    messages.value = messages.value + Message(text = assistantResponse, isUser = false)
                  } else {
                      // Handle error
                      val errorBody = response?.errorBody()?.string()
                      messages.value = messages.value + Message(text = "Error: ${response?.code()} - $errorBody", isUser = false)
                  }

              } catch (e: Exception) {
                  // Handle exception
                  messages.value = messages.value + Message(text = "Exception: ${e.localizedMessage}", isUser = false)
              }
          }
      }

      private fun buildOpenAIMessageContent(message: Message): List<OpenAIMessage.Content> {
        return if (message.imageUrl != null) {
          listOf(
            OpenAIMessage.Content(type = "text", text = message.text),
            OpenAIMessage.Content(type = "image_url", image_url = OpenAIMessage.ImageUrl(url = "data:image/jpeg;base64,${message.imageUrl}"))
          )
        } else {
          listOf(OpenAIMessage.Content(type = "text", text = message.text))
        }
      }

      private fun buildOpenRouterMessageContent(message: Message): String {
          // OpenRouter doesn't have a built-in multimodal format like OpenAI's in its standard API.
          // You would typically send images as base64 within the text, or use a separate multimodal model/API.
          return if (message.imageUrl != null) {
              "${message.text}\n\nImage: data:image/jpeg;base64,${message.imageUrl}"  // Combine text and image
          } else {
              message.text
          }
      }

        private fun encodeImageToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }

    enum class APIType {
        OPENAI,
        OPENROUTER,
        MULTIMODAL
    }
