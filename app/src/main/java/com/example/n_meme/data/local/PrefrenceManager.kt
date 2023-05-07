package com.example.n_meme.data.local

import android.content.SharedPreferences

class PreferenceManager(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_DISPLAY_NAME = "displayName"
    }

    fun saveEmail(email: String) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun saveDisplayName(displayName: String) {
        sharedPreferences.edit().putString(KEY_DISPLAY_NAME, displayName).apply()
    }

    fun getDisplayName(): String? {
        return sharedPreferences.getString(KEY_DISPLAY_NAME, null)
    }
}