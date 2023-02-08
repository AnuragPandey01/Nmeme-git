package com.example.n_meme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.n_meme.databinding.FragmentCategoryBrowseBinding


class CategoryBrowseFragment : Fragment() {

    companion object{
        val categories = listOf(
            Pair("Wholesome","wholesomememes"),
            Pair("Anime","GoodAnimemes"),
            Pair("Art","artmemes"),
            Pair("Cursed","cursedcomments"),
            Pair("Programmer","ProgrammerHumor"),
            Pair("General","memes"),
            Pair("History","historymemes"),
        )
    }

    private var _binding: FragmentCategoryBrowseBinding? = null
    private val binding: FragmentCategoryBrowseBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //data binding
        _binding = FragmentCategoryBrowseBinding.inflate(inflater)

        binding.rvCategory.adapter = CategoryBrowseAdapter(categories){  categoryIndex ->
            val action = CategoryBrowseFragmentDirections.actionCategoryBrowseFragmentToFeedFragment(categoryIndex)
            findNavController().navigate(action)
        }
        binding.rvCategory.layoutManager = GridLayoutManager(requireContext(),2)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

}