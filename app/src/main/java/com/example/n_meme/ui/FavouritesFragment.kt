package com.example.n_meme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.n_meme.R
import com.example.n_meme.adapter.FavAdapter
import com.example.n_meme.databinding.FragmentFavouritesBinding
import com.example.n_meme.model.FavDataBase
import com.example.n_meme.model.Favourites


class FavouritesFragment : Fragment() {

    private lateinit var favDataBaseInstance: FavDataBase
    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_favourites,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateFavList()
    }

    private fun initRecyclerView(favList: List<Favourites>) {
        val adapter = FavAdapter(requireContext() , favList)
        val layoutManager = GridLayoutManager(context,3)
        binding.favRecyclerview.adapter = adapter
        binding.favRecyclerview.layoutManager = layoutManager

    }

    private fun updateFavList() {
        favDataBaseInstance = FavDataBase.getDatabaseInstance(requireContext())
        favDataBaseInstance.favDao().getFav().observe(viewLifecycleOwner){
            initRecyclerView(it)
        }

    }
}