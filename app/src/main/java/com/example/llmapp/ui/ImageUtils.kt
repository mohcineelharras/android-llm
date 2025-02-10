package com.example.llmapp.ui;

    import android.content.ContentResolver
    import android.graphics.Bitmap
    import android.graphics.BitmapFactory
    import android.net.Uri
    import java.io.InputStream

    object ImageUtils {
        fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
            var inputStream: InputStream? = null
            return try {
                inputStream = contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                inputStream?.close()
            }
        }
    }
