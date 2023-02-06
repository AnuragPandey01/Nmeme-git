package com.example.n_meme.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.n_meme.R
import com.example.n_meme.databinding.ActivityLoginBinding
import com.example.n_meme.ui.MainActivity
import com.example.n_meme.util.hideKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding
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

        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setUpOnClickListener()
        setContentView(binding.root)
    }

    private fun setUpOnClickListener() {
        binding.goToSignupBtn.setOnClickListener {
            navigateToSignupActivity()
        }
        binding.loginButton.setOnClickListener { loginUser() }
        binding.btnResetPassword.setOnClickListener {
            Toast.makeText(this,"mail at nmeme-support@droidev.xyz to initiate password reset email",Toast.LENGTH_LONG).show()
        }
    }

    private fun loginUser() {

        hideKeyboard()
        toggleInputs(false)
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.visibility = View.GONE
        binding.btnResetPassword.visibility = View.GONE


        val email = binding.loginInput.text.toString()
        val password = binding.passwordInput.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = task.result.user!!
                    if (!currentUser.isEmailVerified) {
                        showVerificationDialog(currentUser)
                        return@addOnCompleteListener
                    }
                    navigateToMainActivity()
                    return@addOnCompleteListener
                }
                binding.tvError.text = "Incorrect username or password"
                binding.tvError.visibility = View.VISIBLE
                binding.loginButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.btnResetPassword.visibility = View.VISIBLE
                toggleInputs(true)
            }
    }

    private fun showVerificationDialog(currentUser: FirebaseUser) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Verify email")
            .setMessage("looks like your email is not verified.Press verify to proceed")
            .setPositiveButton("verify"){ dialog, _ ->
                sendEmailVerification(currentUser)
                dialog.dismiss()
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun toggleInputs(enabled: Boolean) {
        binding.textEmailLayout.isEnabled = enabled
        binding.passwordInputLayout.isEnabled = enabled
        binding.goToSignupBtn.isEnabled = enabled
    }

    private fun navigateToSignupActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun sendEmailVerification(currentUser: FirebaseUser) {

        toggleInputs(true)
        binding.loginButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.btnResetPassword.visibility = View.VISIBLE
        currentUser.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snackbar = Snackbar.make(
                    binding.root,
                    "A verification email is sent on your email address",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.green_200))
                snackbar.setTextColor(ContextCompat.getColor(this, R.color.green_500))
                snackbar.show()

                binding.tvError.text = "verify using the verification link and then login again"
                binding.tvError.visibility = View.VISIBLE
                return@addOnCompleteListener
            }
            binding.tvError.text =
                "some error occurred while the sending verification email. Try again later"
            binding.tvError.visibility = View.VISIBLE
        }
    }


}