package com.example.n_meme.ui.auth.intent

sealed class LoginIntent {
    class LoginUser(val email: String,val password: String): LoginIntent()
    object SendVerificationEmail: LoginIntent()
    object CheckSignedIn: LoginIntent()
}