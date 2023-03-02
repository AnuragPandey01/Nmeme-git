package com.example.n_meme.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.n_meme.R
import com.example.n_meme.databinding.ActivitySignUpBinding
import com.example.n_meme.ui.MainActivity
import com.example.n_meme.util.hideKeyboard
import com.example.n_meme.util.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest

class SignUpActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private var _binding: ActivitySignUpBinding? = null
    private val binding: ActivitySignUpBinding
        get() = _binding!!

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            navigateToMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            signupUser()
        }
        binding.goToLoginBtn.setOnClickListener {
            navigateToLoginActivity()
        }


    }

    private fun signupUser() {

        //update ui
        hideKeyboard()
        //validate
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val confirmPassword = binding.confirmPasswordInput.text.toString()

        if (password != confirmPassword) {
            binding.textConfirmPasswordLayout.error = "password and confirm password should be same"
            return
        } else if (!email.isValidEmail()) {
            binding.textEmailLayout.error = "please enter a valid email"
            return
        }

        //signup begins
        toggleInputs(false)
        binding.signupButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.goToLoginBtn.visibility = View.GONE

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = binding.displayNameInput.text.toString()
                    }
                    task.result.user?.updateProfile(profileUpdates)
                    Toast.makeText(this,"you are now registered",Toast.LENGTH_LONG).show()
                    navigateToLoginActivity()
                } else {
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    toggleInputs(true)
                    binding.signupButton.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.goToLoginBtn.visibility = View.VISIBLE
                }
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

    private fun toggleInputs(enabled: Boolean) {
        binding.emailInput.isEnabled = enabled
        binding.passwordInput.isEnabled = enabled
        binding.confirmPasswordInput.isEnabled = enabled
    }


}