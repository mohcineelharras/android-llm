package com.example.llmapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.llmapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.buttonSend.setOnClickListener {
      // TODO: Implement sending message to LLM API
    }

    binding.imageView.setOnClickListener {
      // TODO: Implement image selection
    }
  }
}
