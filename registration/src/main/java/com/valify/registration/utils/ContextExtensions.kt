package com.valify.registration.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hiddenKeyboard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun Context.showKeyboard(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}