package com.valify.registration.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.UUID

private const val KEY_ALIAS = "verify-db-key"
private const val PREFS_NAME = "verify_secure_prefs"

fun generateSecretKey(context: Context): String {
    val encryptedPrefs = getEncryptedSharedPreferences(context)
    val storedPassphrase = encryptedPrefs.getString(KEY_ALIAS, null) ?: generateAndStorePassphrase(encryptedPrefs)
    return storedPassphrase
}

private fun generateAndStorePassphrase(encryptedPrefs: SharedPreferences): String {
    val newPassphrase = UUID.randomUUID().toString()
    encryptedPrefs.edit().putString(KEY_ALIAS, newPassphrase).apply()
    return newPassphrase
}

private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
