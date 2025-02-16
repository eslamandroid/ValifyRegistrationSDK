package com.valify.registration.presentation.captureImage.viewmodel

import android.graphics.Bitmap
import androidx.annotation.Keep

@Keep
sealed class CaptureIntent {
    data class SaveCaptureImageIntent(val image: Bitmap) : CaptureIntent()
 }