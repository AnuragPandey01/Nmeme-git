package com.example.n_meme.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.n_meme.databinding.FragmentSearchBinding
import com.example.n_meme.util.Subreddits
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter : SearchAdapter
    private var _binding: FragmentSearchBinding?  = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchAdapter = SearchAdapter { subreddit ->
            viewModel.saveSubreddit(subreddit)
            findNavController().navigateUp()
        }.apply {
            subredditList = Subreddits.list
        }
        setUpRecyclerView()
        setOnClickListener()
        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.apply {
            searchRecyclerView.apply {
                adapter = searchAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

        }
    }

    private fun setOnClickListener() {
        binding.apply {
            searchInput.doAfterTextChanged { text ->
                searchAdapter.filterList(Subreddits.list.filter { subreddit ->
                    subreddit.contains(text.toString(), true)
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}