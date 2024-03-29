package com.example.n_meme.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.n_meme.data.local.PreferenceManager
import com.example.n_meme.ui.auth.intent.LoginIntent
import com.example.n_meme.ui.auth.viewState.LoginState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
): ViewModel() {

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
                    is LoginIntent.CheckSignedIn -> {
                        firebaseAuth.currentUser?.let { user ->
                            if(user.isEmailVerified){
                                _state.value = LoginState.AlreadySignedIn
                            }
                        }
                    }
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
                    preferenceManager.saveEmail(email)
                    preferenceManager.saveDisplayName(currentUser.displayName.toString())
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