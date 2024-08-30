package com.marcelos.agendadecontatos.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageProcessing {

    suspend fun decodeImageFromUri(context: Context, uri: Uri): ImageBitmap {
        return withContext(Dispatchers.IO) {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            }
            bitmap.asImageBitmap()
        }
    }

    suspend fun correctImageRotation(bitmap: Bitmap): Bitmap {
        return withContext(Dispatchers.IO) {
            val matrix = Matrix().apply { postRotate(ROTATION_IMAGE) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }

    private const val ROTATION_IMAGE = 90f
}