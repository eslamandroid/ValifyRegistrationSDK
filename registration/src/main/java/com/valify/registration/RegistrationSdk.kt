package com.valify.registration

import android.content.Context
import android.content.Intent
import androidx.annotation.Keep
import com.valify.registration.presentation.MainActivity

@Keep
object RegistrationSDK {
    fun startRegistration(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}