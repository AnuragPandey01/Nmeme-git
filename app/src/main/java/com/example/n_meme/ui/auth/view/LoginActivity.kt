package com.example.n_meme.ui.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.n_meme.R
import com.example.n_meme.databinding.ActivityLoginBinding
import com.example.n_meme.ui.MainActivity
import com.example.n_meme.ui.auth.Intent.LoginIntent
import com.example.n_meme.ui.auth.viewState.LoginState
import com.example.n_meme.ui.auth.viewmodel.LoginViewModel
import com.example.n_meme.util.hideKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setUpOnClickListener()
        observeState()
    }

    private fun setUpOnClickListener() {
        binding.goToSignupBtn.setOnClickListener {
            navigateToSignupActivity()
        }

        binding.loginButton.setOnClickListener {
            hideKeyboard()

            val email = binding.loginInput.text.toString()
            val password = binding.passwordInput.text.toString()

            lifecycleScope.launch {
                viewModel.intent.send(LoginIntent.LoginUser(email, password))
            }
        }

        binding.btnResetPassword.setOnClickListener {
            Toast.makeText(this,"mail at nmeme-support@droidev.xyz to initiate password reset email",Toast.LENGTH_LONG).show()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is LoginState.Idle -> {}

                    is LoginState.Loading ->{
                        clearError()
                        toggleLoading(true)
                    }

                    is LoginState.LoginSuccess ->{
                        toggleLoading(false)
                        navigateToMainActivity()
                    }

                    is LoginState.UnverifiedEmail ->{
                        toggleLoading(false)
                        showVerificationDialog()
                    }

                    is LoginState.Error -> {
                        toggleLoading(false)
                        setError("Incorrect Username or password")
                    }

                    is LoginState.EmailSendSuccess ->{
                        toggleLoading(false)
                        val snackbar = Snackbar.make(
                            binding.root,
                            "A verification email is sent on your email address",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setBackgroundTint(ContextCompat.getColor(this@LoginActivity, R.color.green_200))
                        snackbar.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
                        snackbar.show()
                        setError("A verification email is sent on your email addres")
                    }

                    is LoginState.EmailSendFailure ->{
                        toggleLoading(false)
                        setError("some error occurred while the sending verification email. Try again later")
                    }
                }
            }
        }
    }

    private fun showVerificationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Verify email")
            .setMessage("looks like your email is not verified.Press verify to proceed")
            .setPositiveButton("verify"){ dialog, _ ->
                lifecycleScope.launch {
                    viewModel.intent.send(LoginIntent.SendVerificationEmail)
                }
                dialog.dismiss()
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun toggleLoading(loading: Boolean) {

        if(loading){
            binding.progressBar.visibility = View.VISIBLE
            binding.goToSignupBtn.visibility = View.GONE
            binding.btnResetPassword.visibility = View.GONE
            binding.loginButton.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.goToSignupBtn.visibility = View.VISIBLE
            binding.btnResetPassword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE
        }
        binding.textEmailLayout.isEnabled = !loading
        binding.passwordInputLayout.isEnabled = !loading
        binding.goToSignupBtn.isEnabled = !loading
    }

    private fun setError(errorMsg: String){
        binding.tvError.text = errorMsg
        binding.tvError.visibility = View.VISIBLE
    }

    private fun clearError(){
        binding.tvError.visibility = View.GONE
    }

    private fun navigateToSignupActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}