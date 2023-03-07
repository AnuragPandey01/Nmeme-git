package com.example.n_meme.ui.auth.viewState

sealed class SignupState {
    object Idle: SignupState()
    object Loading : SignupState()
    object SignUpSuccess : SignupState()
    class SignUpError(val message: String) : SignupState()
    object InvalidEmail: SignupState()
    object UserVerified : SignupState()
}