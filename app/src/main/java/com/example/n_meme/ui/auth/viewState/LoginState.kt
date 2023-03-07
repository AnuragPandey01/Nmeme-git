package com.example.n_meme.ui.auth.viewState

sealed class LoginState {
    object Idle: LoginState()
    object Loading : LoginState()
    object LoginSuccess: LoginState()
    object UnverifiedEmail: LoginState()
    class Error(val message: String): LoginState()
    object EmailSendSuccess : LoginState()
    object EmailSendFailure : LoginState()
}