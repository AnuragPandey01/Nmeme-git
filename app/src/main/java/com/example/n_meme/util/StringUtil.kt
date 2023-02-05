package com.example.n_meme.util

import android.text.TextUtils
import android.util.Patterns

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
