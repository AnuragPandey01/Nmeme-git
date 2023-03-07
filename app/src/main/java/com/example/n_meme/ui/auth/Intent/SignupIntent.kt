package com.example.n_meme.ui.auth.Intent

sealed class SignupIntent {
    class SignupUser(val displayName : String,val email: String,val password: String): SignupIntent()
    object CheckSignedIn: SignupIntent()
}