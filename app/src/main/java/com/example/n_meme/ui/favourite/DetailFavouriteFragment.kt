package com.example.n_meme.ui.favourite

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.n_meme.databinding.FragmentDetailFavouriteBinding
import com.example.n_meme.data.local.Favourites
import com.example.n_meme.ui.base.BaseFragment
import com.example.n_meme.ui.favourite.adapter.DetailFavAdapter
import com.example.n_meme.util.ImageSaver
import com.example.n_meme.util.Share
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFavouriteFragment : BaseFragment() {

    private val favViewModel : FavViewModel by lazy {
        ViewModelProvider(this).get(FavViewModel::class.java)
    }
    private val args : DetailFavouriteFragmentArgs by lazy {
        DetailFavouriteFragmentArgs.fromBundle(requireArguments())
    }
    private val favList: LiveData<List<Favourites>> by lazy{
        favViewModel.GetAllFav().favList
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
        favList.observe(viewLifecycleOwner){
            binding.viewpager.adapter = DetailFavAdapter(requireContext(), it )
            binding.viewpager.setCurrentItem(args.toFav,false)
        }

        binding.downloadMeme.setOnClickListener {
            Glide.with(requireContext())
                .asBitmap()
                .load(favList.value!![binding.viewpager.currentItem].url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        ImageSaver.saveImage(requireContext(),resource,"meme")
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

        }

        binding.shareMeme.setOnClickListener {
            Share.asUrl(requireContext(),favList.value!![binding.viewpager.currentItem].url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}