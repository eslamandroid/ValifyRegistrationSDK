package com.valify.registration.presentation.registration.viewmodel

import androidx.annotation.Keep
import com.valify.registration.domain.model.UserModel

@Keep
sealed class RegistrationIntent {
    data class SaveUserIntent(val input: UserModel) : RegistrationIntent()
    data object ResetState:RegistrationIntent()
}