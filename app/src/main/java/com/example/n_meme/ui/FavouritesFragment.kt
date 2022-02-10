package com.example.n_meme.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.n_meme.R
import com.example.n_meme.adapter.FavAdapter
import com.example.n_meme.databinding.FragmentFavouritesBinding
import com.example.n_meme.model.FavDataBase
import com.example.n_meme.model.Favourites


class FavouritesFragment : Fragment() {

    private lateinit var favDataBaseInstance: FavDataBase
    private var favList = mutableListOf<Favourites>()
    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateFavList()
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_favourites,container,false)

        binding.load.setOnClickListener {
            Log.d("TAG", "onCreateView: $favList")
            initRecyclerView()
        }
        return  binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() {
        val adapter = FavAdapter(requireContext(),favList)
        val layoutManager = GridLayoutManager(requireContext(),3)
        binding.favRecyclerview.adapter = adapter
        binding.favRecyclerview.layoutManager = layoutManager
    }


    private fun updateFavList() {
        favDataBaseInstance = FavDataBase.getDatabaseInstance(requireActivity())
        favDataBaseInstance.favDao().getFav().observe(requireActivity()){
            Log.d("TAG", "onCreateView: $it")
            favList.addAll(it)
            Log.d("TAG", "onCreateView: $favList")
        }
    }




}