package com.valify.registration.presentation.registration.viewmodel

import androidx.annotation.Keep
import com.valify.registration.domain.utils.AppError

@Keep
data class RegistrationState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val appFailure: AppError? = null,
)

