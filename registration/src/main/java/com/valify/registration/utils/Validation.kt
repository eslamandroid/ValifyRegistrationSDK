package com.valify.registration.utils


fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun String.isValidPhone() = matches(Regex("^[0-9]{11}$"))
fun String.isValidPassword() = length >= 8


