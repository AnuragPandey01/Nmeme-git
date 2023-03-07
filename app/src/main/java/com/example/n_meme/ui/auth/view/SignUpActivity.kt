package com.example.n_meme.ui.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.n_meme.databinding.ActivitySignUpBinding
import com.example.n_meme.ui.MainActivity
import com.example.n_meme.ui.auth.Intent.SignupIntent
import com.example.n_meme.ui.auth.viewState.SignupState
import com.example.n_meme.ui.auth.viewmodel.SignupViewModel
import com.example.n_meme.util.hideKeyboard
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignupViewModel>()
    private var _binding: ActivitySignUpBinding? = null
    private val binding: ActivitySignUpBinding
        get() = _binding!!

    public override fun onStart() {
        super.onStart()
        lifecycleScope.launch{
            viewModel.intent.send(SignupIntent.CheckSignedIn)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setOnClickListener()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is SignupState.Idle -> {}
                    is SignupState.InvalidEmail -> {
                        binding.emailInput.error = "please enter a valid email"
                    }
                    is SignupState.Loading -> {
                        toggleLoadingState(false)
                    }
                    is SignupState.SignUpSuccess -> {
                        navigateToLoginActivity()
                    }
                    is SignupState.SignUpError ->{
                        toggleLoadingState(true)
                    }
                    is SignupState.UserVerified -> {
                        navigateToMainActivity()
                    }
                }
            }
        }
    }

    private fun setOnClickListener() {

        binding.signupButton.setOnClickListener {
            //
            hideKeyboard()
            //
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val displayName = binding.displayNameInput.text.toString()

            lifecycleScope.launch {
                viewModel.intent.send(SignupIntent.SignupUser(displayName,email,password))
            }
        }

        binding.goToLoginBtn.setOnClickListener {
            navigateToLoginActivity()
        }
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toggleLoadingState(enabled: Boolean) {
        if(enabled){
            binding.progressBar.visibility = View.GONE
            binding.signupButton.visibility = View.VISIBLE
            binding.goToLoginBtn.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.VISIBLE
            binding.signupButton.visibility = View.GONE
            binding.goToLoginBtn.visibility = View.GONE
        }

        binding.emailInput.isEnabled = enabled
        binding.passwordInput.isEnabled = enabled
    }


}