package com.valify.registration.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream


fun Bitmap.bitmapToBase64():String {
    val outputStream  = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT)
}