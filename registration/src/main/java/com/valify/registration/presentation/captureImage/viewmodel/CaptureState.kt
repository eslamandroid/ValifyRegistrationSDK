package com.valify.registration.presentation.captureImage.viewmodel

import androidx.annotation.Keep
import com.valify.registration.domain.utils.AppError

@Keep
data class CaptureState(
    val success: Boolean = false,
    val appFailure: AppError? = null,
)

