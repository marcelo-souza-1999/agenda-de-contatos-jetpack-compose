package com.marcelos.agendadecontatos.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

private const val QUALITY_IMAGE = 100

fun ImageBitmap?.toByteArray(): ByteArray? {
    return this?.asAndroidBitmap()?.let { bitmap ->
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_IMAGE, stream)
        stream.toByteArray()
    }
}

fun ByteArray?.toImageBitmap(): ImageBitmap? {
    return this?.let { byteArray ->
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        bitmap?.let { ImageBitmap(bitmap.width, bitmap.height, ImageBitmapConfig.Argb8888) }
    }
}