package com.example.n_meme.ui.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.n_meme.R
import com.example.n_meme.databinding.FragmentLoginBinding
import com.example.n_meme.ui.auth.intent.LoginIntent
import com.example.n_meme.ui.auth.viewState.LoginState
import com.example.n_meme.ui.auth.viewmodel.LoginViewModel
import com.example.n_meme.ui.base.BaseFragment
import com.example.n_meme.util.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val viewModel by viewModels<LoginViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.intent.send(LoginIntent.CheckSignedIn)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        setUpOnClickListener()
        observeState()
        return binding.root
    }

    private fun setUpOnClickListener() {
        binding.goToSignupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.loginButton.setOnClickListener {
            //requireContext().hideKeyboard()

            val email = binding.loginInput.text.toString()
            val password = binding.passwordInput.text.toString()

            lifecycleScope.launch {
                viewModel.intent.send(LoginIntent.LoginUser(email, password))
            }
        }

        binding.btnResetPassword.setOnClickListener {
            requireContext().showToast("mail at nmeme-support@droidev.xyz to initiate password reset email")
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is LoginState.Idle -> {}

                    is LoginState.Loading -> {
                        clearError()
                        toggleLoading(true)
                    }

                    is LoginState.LoginSuccess -> {
                        toggleLoading(false)
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedFragment())
                    }

                    is LoginState.UnverifiedEmail -> {
                        toggleLoading(false)
                        showVerificationDialog()
                    }

                    is LoginState.Error -> {
                        toggleLoading(false)
                        setError("Incorrect Username or password")
                    }

                    is LoginState.EmailSendSuccess -> {
                        toggleLoading(false)
                        val snackbar = Snackbar.make(
                            binding.root,
                            "A verification email is sent on your email address",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setBackgroundTint(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green_200
                            )
                        )
                        snackbar.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        snackbar.show()
                        setError("A verification email is sent on your email addres")
                    }

                    is LoginState.EmailSendFailure -> {
                        toggleLoading(false)
                        setError("some error occurred while the sending verification email. Try again later")
                    }

                    is LoginState.AlreadySignedIn ->{
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedFragment())
                    }
                }
            }
        }
    }

    private fun showVerificationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Verify email")
            .setMessage("looks like your email is not verified.Press verify to proceed")
            .setPositiveButton("verify") { dialog, _ ->
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

        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.goToSignupBtn.visibility = View.GONE
            binding.btnResetPassword.visibility = View.GONE
            binding.loginButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.goToSignupBtn.visibility = View.VISIBLE
            binding.btnResetPassword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE
        }
        binding.textEmailLayout.isEnabled = !loading
        binding.passwordInputLayout.isEnabled = !loading
        binding.goToSignupBtn.isEnabled = !loading
    }

    private fun setError(errorMsg: String) {
        binding.tvError.text = errorMsg
        binding.tvError.visibility = View.VISIBLE
    }

    private fun clearError() {
        binding.tvError.visibility = View.GONE
    }
}