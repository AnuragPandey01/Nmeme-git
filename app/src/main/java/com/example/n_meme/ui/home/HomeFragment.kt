package com.example.n_meme.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.n_meme.R
import com.example.n_meme.databinding.FragmentHomeBinding
import com.example.n_meme.model.Meme
import com.example.n_meme.ui.home.adapter.MemeAdapter
import java.lang.Exception

const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var addedToFavourites = mutableListOf<String>()
    private var _binding: FragmentHomeBinding? = null
    val binding : FragmentHomeBinding
        get() = _binding!!

    private val memeAdapter : MemeAdapter by lazy { MemeAdapter(requireContext()) }
    private val memeList: List<Meme> by lazy { memeAdapter.memeList }
    private val viewModel : HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //data binding
        _binding = FragmentHomeBinding.inflate(inflater)
        setOnClickListener()
        initViewPager()
        loadMeme()
        try {
            viewModel.response.observe(viewLifecycleOwner){ response->
                if(response.isSuccessful){
                    memeAdapter.setData(response.body()!!.memes)
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_LONG).show()
                    Log.e(TAG, "onCreateView: ${response.errorBody()} " )
                }
            }
        }
        catch (e:Exception){
            Log.e(TAG, "onCreateView: ${e.stackTrace}")
            Toast.makeText(requireContext(),"Check your connection",Toast.LENGTH_LONG).show()
        }

        return binding.root
    }


    private fun setOnClickListener() {

        binding.apply {
            shareMeme.setOnClickListener { shareMeme() }
            favMeme.setOnClickListener { addToFav() }
        }
    }

    private fun loadMeme(){
        if( memeList.size%7 == 0){
            viewModel.getMeme("dankmemes")
        }
    }

    private fun initViewPager(){
        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewpager.adapter = memeAdapter

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            var prevPosition = 0
            override fun onPageSelected(position: Int) {
                if( prevPosition < position && memeList.size - binding.viewpager.currentItem <= 3){
                   loadMeme()
                }
                prevPosition = position
            }
        })
    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey check out this meme ${memeList[binding.viewpager.currentItem].url}")
        val chooser = Intent.createChooser(intent, "Complete action using...")
        startActivity(chooser)
    }

    private fun addToFav() {
        val toBeAdded = memeList[binding.viewpager.currentItem]

        if(addedToFavourites.contains(toBeAdded.url)){
            Toast.makeText(requireContext(),"already added", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.addToFav(toBeAdded.url,toBeAdded.title)

        Toast.makeText(requireContext(),"added to favourite", Toast.LENGTH_SHORT).show()
        addedToFavourites.add(toBeAdded.url)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}