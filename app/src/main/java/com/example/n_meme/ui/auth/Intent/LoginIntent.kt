package com.example.n_meme.ui.auth.Intent

sealed class LoginIntent {
    class LoginUser(val email: String,val password: String): LoginIntent()
    object SendVerificationEmail: LoginIntent()
}