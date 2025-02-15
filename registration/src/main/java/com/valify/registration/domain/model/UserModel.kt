package com.valify.registration.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class UserModel(val username: String, val phone: String, val email: String, val password: String) : Parcelable
