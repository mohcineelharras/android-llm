package com.example.llmapp.data
    import com.google.gson.annotations.SerializedName

    data class OpenAIMessage(
      val role: String,
      val content: List<Content>
    ) {
      data class Content(
        val type: String,
        val text: String? = null,
        @SerializedName("image_url") val image_url: ImageUrl? = null,
      )
      data class ImageUrl(
        val url: String
      )
    }
