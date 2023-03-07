package com.example.n_meme.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.n_meme.ui.auth.Intent.LoginIntent
import com.example.n_meme.ui.auth.viewState.LoginState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val intent = Channel<LoginIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state : StateFlow<LoginState>
        get() = _state

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init{
        observeIntent()
    }

    private fun observeIntent(){
        viewModelScope.launch {
            intent.consumeAsFlow().collect{
                when(it){
                    is LoginIntent.LoginUser -> loginUser(it.email, it.password)
                    is LoginIntent.SendVerificationEmail -> sendEmailVerification()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        _state.value = LoginState.Loading

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val currentUser = task.result.user!!
                    if (!currentUser.isEmailVerified) {
                        _state.value = LoginState.UnverifiedEmail
                        return@addOnCompleteListener
                    }

                    _state.value = LoginState.LoginSuccess
                }else{
                    _state.value = LoginState.Error(task.exception?.message.toString())
                }
            }
    }

    private fun sendEmailVerification() {
        firebaseAuth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.value = LoginState.EmailSendSuccess
            }else{
                _state.value = LoginState.EmailSendFailure
            }

        }
    }


}