package com.example.llmapp.ui

    import android.graphics.Bitmap
    import android.net.Uri
    import android.util.Log
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.PickVisualMediaRequest
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Send
    import androidx.compose.material3.Button
    import androidx.compose.material3.DropdownMenu
    import androidx.compose.material3.DropdownMenuItem
    import androidx.compose.material3.Icon
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.asImageBitmap
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.unit.dp
    import com.example.llmapp.LLMViewModel
    import com.example.llmapp.data.Message
    import coil.compose.rememberAsyncImagePainter
    import coil.request.ImageRequest
    import com.example.llmapp.LLMViewModel.APIType

    @Composable
    fun ChatScreen(viewModel: LLMViewModel) {
        val messages = viewModel.messages.value
        var apiKey by remember { mutableStateOf("") }
        var selectedApiType by remember { mutableStateOf(APIType.OPENAI) }
        var showApiMenu by remember { mutableStateOf(false) }

        val context = LocalContext.current

        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri: Uri? ->
              uri?.let {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flag)

                val bitmap = ImageUtils.getBitmapFromUri(context.contentResolver, uri)
                viewModel.onImageSelected(bitmap)
              }
            }
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // API Key Input
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("API Key") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // API Selection Button
            Box(modifier = Modifier.padding(8.dp)) {
                Button(onClick = { showApiMenu = true }) {
                    Text("API: ${selectedApiType.name}")
                }
                DropdownMenu(
                    expanded = showApiMenu,
                    onDismissRequest = { showApiMenu = false }
                ) {
                    APIType.values().forEach { apiType ->
                        DropdownMenuItem(onClick = {
                            selectedApiType = apiType
                            showApiMenu = false
                        }, text = { Text(apiType.name) })
                    }
                }
            }


            // Chat Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    ChatMessage(message)
                }
            }

            // Image Preview
            viewModel.selectedImage.value?.let { bitmap ->
              Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                  .size(100.dp)
                  .padding(8.dp)
              )
            }

            // Input and Send Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.inputText.value,
                    onValueChange = { viewModel.onInputTextChanged(it) },
                    label = { Text("Message") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                  singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                },
                modifier = Modifier.height(56.dp)) {
                  Text("Select Image")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (apiKey.isNotBlank()) {
                            viewModel.sendMessage(apiKey, selectedApiType)
                        } else {
                            // Handle missing API key
                            Log.e("ChatScreen", "API Key is missing")
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }

    @Composable
    fun ChatMessage(message: Message) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!message.isUser) {
                // Display a small bot icon (optional)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .padding(end = 8.dp)
                        .align(Alignment.Top)
                ) {
                    // You can place an Image or Icon here to represent the bot
                    Text("🤖") // Simple bot emoji as placeholder
                }
            }

            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = if (message.isUser) 16.dp else 4.dp,
                            topEnd = if (message.isUser) 4.dp else 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .padding(8.dp)
            ) {
                if (message.imageUrl != null) {
                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data("data:image/jpeg;base64,${message.imageUrl}")
                        .build()
                    Image(
                        painter = rememberAsyncImagePainter(imageRequest),
                        contentDescription = "Image from message",
                        modifier = Modifier
                            .height(200.dp) // Adjust size as needed
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(text = message.text)
            }
        }
    }
