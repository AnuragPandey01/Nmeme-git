package com.example.n_meme.ui.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.n_meme.databinding.FragmentSignupBinding
import com.example.n_meme.ui.auth.intent.SignupIntent
import com.example.n_meme.ui.auth.viewState.SignupState
import com.example.n_meme.ui.auth.viewmodel.SignupViewModel
import com.example.n_meme.ui.base.BaseFragment
import kotlinx.coroutines.launch

class SignupFragment :BaseFragment() {

    private val viewModel by viewModels<SignupViewModel>()
    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater)
        setOnClickListener()
        observeState()
        return binding.root
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
                        findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                    }
                    is SignupState.SignUpError ->{
                        toggleLoadingState(true)
                    }
                }
            }
        }
    }

    private fun setOnClickListener() {

        binding.signupButton.setOnClickListener {
            //
            //hideKeyboard()
            //
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val displayName = binding.displayNameInput.text.toString()

            lifecycleScope.launch {
                viewModel.intent.send(SignupIntent.SignupUser(displayName,email,password))
            }
        }

        binding.goToLoginBtn.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }
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