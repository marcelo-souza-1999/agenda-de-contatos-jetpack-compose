package com.marcelos.agendadecontatos.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.marcelos.agendadecontatos.utils.Constants.EXTENSION_IMAGE
import com.marcelos.agendadecontatos.utils.Constants.FILE_PATH_IMAGE_NAME
import com.marcelos.agendadecontatos.utils.Constants.QUALITY_IMAGE
import java.io.File

fun saveToPathImage(
    context: Context,
    imageBitmap: ImageBitmap?
): String? {
    val bitmap = imageBitmap?.asAndroidBitmap()

    val fileName = "$FILE_PATH_IMAGE_NAME${System.currentTimeMillis()}$EXTENSION_IMAGE"
    val file = File(context.filesDir, fileName)

    file.outputStream().use { outputStream ->
        bitmap?.compress(Bitmap.CompressFormat.PNG, QUALITY_IMAGE, outputStream)
    }

    return file.absolutePath
}