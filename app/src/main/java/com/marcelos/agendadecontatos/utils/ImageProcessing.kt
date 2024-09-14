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

            val width = (bitmap.width * SCALE_FACTOR).toInt()
            val height = (bitmap.height * SCALE_FACTOR).toInt()
            val reducedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

            val matrix = Matrix().apply { postRotate(ROTATION_IMAGE) }
            Bitmap.createBitmap(
                reducedBitmap, 0, 0,
                reducedBitmap.width, reducedBitmap.height, matrix, false
            )
        }
    }

    private const val ROTATION_IMAGE = 90f
    private const val SCALE_FACTOR = 0.7f
}