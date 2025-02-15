package com.valify.registration

import android.content.Context
import android.content.Intent
import androidx.annotation.Keep
import com.valify.registration.presentation.HostActivity

@Keep
object RegistrationSDK {
    fun startRegistration(context: Context) {
        val intent = Intent(context, HostActivity::class.java)
        context.startActivity(intent)
    }
}