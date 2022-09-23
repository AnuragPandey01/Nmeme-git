package com.example.n_meme.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.n_meme.ui.favourite.adapter.FavAdapter
import com.example.n_meme.databinding.FragmentFavouritesBinding
import com.example.n_meme.model.database.Favourites


class FavouritesFragment : Fragment() {

    private val favViewModel : FavViewModel by lazy{
        ViewModelProvider(this).get(FavViewModel::class.java)
    }
    private var _binding: FragmentFavouritesBinding? = null
    private val binding : FragmentFavouritesBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateFavList()
    }

    private fun initRecyclerView(favList: List<Favourites>) {
        val adapter = FavAdapter(requireContext() , favList)
        val layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.favRecyclerview.adapter = adapter
        binding.favRecyclerview.layoutManager = layoutManager

    }

    private fun updateFavList() {
        favViewModel.GetAllFav().favList.observe(viewLifecycleOwner){
            initRecyclerView(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}