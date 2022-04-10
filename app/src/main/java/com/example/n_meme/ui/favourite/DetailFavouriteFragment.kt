package com.example.n_meme.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.n_meme.R
import com.example.n_meme.databinding.FragmentDetailFavouriteBinding
import com.example.n_meme.model.database.Favourites
import com.example.n_meme.ui.favourite.adapter.DetailFavAdapter

class DetailFavouriteFragment : Fragment(R.layout.fragment_detail_favourite) {

    private val favViewModel : FavViewModel by lazy {
        ViewModelProvider(this).get(FavViewModel::class.java)
    }
    private val args : DetailFavouriteFragmentArgs by lazy {
        DetailFavouriteFragmentArgs.fromBundle(requireArguments())
    }
    private var _binding : FragmentDetailFavouriteBinding? = null
    private val binding : FragmentDetailFavouriteBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailFavouriteBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favViewModel.GetAllFav().favList.observe(viewLifecycleOwner){
            binding.viewpager.adapter = DetailFavAdapter(requireContext(), it )
            binding.viewpager.setCurrentItem(args.toFav,false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}