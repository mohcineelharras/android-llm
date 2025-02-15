package com.example.llmapp

    import android.graphics.Bitmap
    import android.util.Base64
    import androidx.lifecycle.ViewModel
    import kotlinx.coroutines.launch
    import java.io.ByteArrayOutputStream
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.toRequestBody
    import com.example.llmapp.data.*
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import okhttp3.OkHttpClient
    import okhttp3.logging.HttpLoggingInterceptor
    import androidx.lifecycle.viewModelScope


    class LLMViewModel : ViewModel() {

      val BASE_URL = "https://api.openai.com/v1/"

      private val retrofit: Retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
          .baseUrl(BASE_URL)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
      }

      private val api: LLMApi by lazy {
        retrofit.create(LLMApi::class.java)
      }

      // ... rest of your ViewModel code (sendMessage, etc.) ...
    }

    enum class APIType {
      OPENAI,
      OPENROUTER,
      MULTIMODAL
    }
