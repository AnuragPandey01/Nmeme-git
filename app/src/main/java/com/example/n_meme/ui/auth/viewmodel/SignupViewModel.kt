package com.example.n_meme.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.example.n_meme.ui.auth.Intent.SignupIntent
import com.example.n_meme.ui.auth.viewState.SignupState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.n_meme.util.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    val intent = Channel<SignupIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<SignupState>(SignupState.Idle)
    val state : StateFlow<SignupState>
        get() = _state

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    init{
        observeIntent()
    }

    private fun observeIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect{ it ->
                when(it){
                    is SignupIntent.SignupUser -> signUpUser(it.displayName, it.email, it.password)

                    is SignupIntent.CheckSignedIn -> {
                        firebaseAuth.currentUser?.let { user ->
                            if(user.isEmailVerified){
                                _state.value = SignupState.UserVerified
                            }
                        }
                    }

                }
            }
        }
    }

    private fun signUpUser(displayName : String,email: String,password: String) {

        //return if invalid email
        if(!email.isValidEmail()){
            _state.value = SignupState.InvalidEmail
            return
        }

        _state.value = SignupState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = userProfileChangeRequest {
                        this.displayName = displayName
                    }
                    task.result.user?.updateProfile(profileUpdates)
                    _state.value = SignupState.SignUpSuccess
                }else{
                    _state.value = SignupState.SignUpError(task.exception?.message.toString())
                }
            }
    }
}