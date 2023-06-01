package com.example.n_meme.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.n_meme.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val currentUser : FirebaseUser by lazy{
        firebaseAuth.currentUser!!
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
    get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.tvDisplayName.text = currentUser.displayName
        binding.tvUserEmail.text = currentUser.email

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }

        // currentUser.photoUrl returns null
        /*Glide.with(requireContext())
            .load(currentUser.photoUrl)
            .into(binding.ivProfileImg)*/
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}