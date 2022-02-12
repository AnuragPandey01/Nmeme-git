package com.example.n_meme.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.n_meme.R
import com.example.n_meme.adapter.MemeAdapter
import com.example.n_meme.api.RetrofitInstance
import com.example.n_meme.databinding.FragmentHomeBinding
import com.example.n_meme.model.FavDataBase
import com.example.n_meme.model.Favourites
import com.example.n_meme.model.Meme
import com.example.n_meme.model.MemeResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


class HomeFragment : Fragment() {

    private lateinit var favDataBaseInstance: FavDataBase
    private var memeList = mutableListOf<Meme>()
    private var addedToFavourites = mutableListOf<String>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MemeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //data binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setOnClickListener()
        initViewPager()
        loadMeme()
        favDataBaseInstance = FavDataBase.getDatabaseInstance(requireContext())
        return binding.root
    }

    private fun setOnClickListener() {

        binding.apply {
            shareMeme.setOnClickListener { shareMeme() }
            favMeme.setOnClickListener { addToFav() }
            overflowMenu.setOnClickListener { setPopUpMenu() }
        }
    }

    private fun loadMeme(){
        val call = RetrofitInstance.GetRetrofitInstance.MemeInstance.getMeme("dankmemes")

        call.enqueue(object: Callback<MemeResponse> {
            override fun onResponse(call: Call<MemeResponse>, response: Response<MemeResponse>) {

                val memeResponse  = response.body()

                if(memeResponse != null) {
                    val prevSize = memeList.size
                    memeList.addAll(memeResponse.memes)
                    adapter.notifyItemRangeInserted(prevSize,7)

                }
            }

            override fun onFailure(call: Call<MemeResponse>, t: Throwable) {
                    Toast.makeText(requireContext(),"failed to load more meme",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun initViewPager(){

        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        adapter = MemeAdapter(requireContext() , memeList)
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            var prevPosition = 0
            override fun onPageSelected(position: Int) {
                if( prevPosition < position && memeList.size - binding.viewpager.currentItem <= 3){
                    thread(true){
                        loadMeme()
                    }

                }
                prevPosition = position
            }
        })

        binding.viewpager.adapter = adapter
    }

    private fun setPopUpMenu(){
        val popupMenu = PopupMenu(requireContext(),binding.overflowMenu)

        popupMenu.menuInflater.inflate(R.menu.overflow_menu , popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.setting -> findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
                    R.id.fav -> findNavController().navigate(R.id.action_homeFragment_to_favouritesFragment)
                }
                return true
            }

        })

        popupMenu.show()
    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey check out this meme ${memeList[binding.viewpager.currentItem].url}")
        val chooser = Intent.createChooser(intent, "Complete action using...")
        startActivity(chooser)
    }

    private fun addToFav() {
        val toBeAdded = memeList[binding.viewpager.currentItem].url
        if(addedToFavourites.contains(toBeAdded)){
            Toast.makeText(requireContext(),"already added", Toast.LENGTH_SHORT).show()
            return
        }
        GlobalScope.launch {
            favDataBaseInstance.favDao().insertFav(Favourites(0, toBeAdded) )
        }
        Toast.makeText(requireContext(),"added to favourite", Toast.LENGTH_SHORT).show()
        addedToFavourites.add(toBeAdded)
    }
}