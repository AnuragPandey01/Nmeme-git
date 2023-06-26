package com.example.n_meme.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.n_meme.data.model.Meme
import com.example.n_meme.databinding.FragmentFeedBinding
import com.example.n_meme.ui.base.BaseFragment
import com.example.n_meme.ui.home.adapter.MemeAdapter
import com.example.n_meme.util.ApiResponse
import com.example.n_meme.util.ImageSaver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding: FragmentFeedBinding
        get() = _binding!!

    private var addedToFavourites = mutableListOf<String>()
    private val memeAdapter: MemeAdapter by lazy { MemeAdapter() }
    private val memeList: List<Meme> by lazy { memeAdapter.memeList }
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFeedBinding.inflate(layoutInflater)
        setOnClickListener()
        initViewPager()
        loadMeme()

        viewModel.memeResponseLiveData.observe(viewLifecycleOwner) { res ->
           when(res){
               is ApiResponse.Success -> {
                   binding.progressBar.visibility = View.GONE
                   res.data?.let {
                       memeAdapter.setData(it.memes)
                   }
               }
               is ApiResponse.Error -> {
                   binding.progressBar.visibility = View.GONE
                   MaterialAlertDialogBuilder(requireContext())
                       .setTitle("Error")
                       .setMessage(res.message)
                       .setPositiveButton("Retry") { dialog, _ ->
                           dialog.dismiss()
                           loadMeme()
                       }
                       .setNegativeButton("Cancel") { _, _ ->
                           requireActivity().finish()
                       }
                       .setCancelable(false)
                       .show()
               }

               is ApiResponse.Loading -> {
                   binding.progressBar.visibility = View.VISIBLE
               }
           }
        }

        return binding.root
    }


    private fun setOnClickListener() {
        binding.apply {

            searchMeme.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToSearchFragment())
            }
            shareMeme.setOnClickListener { shareMeme() }
            favMeme.setOnClickListener { addToFav() }
            downloadMeme.setOnClickListener {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(memeList[binding.viewpager.currentItem].url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            saveImage(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }

            binding.btnFav.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToFavouritesFragment())
            }

            binding.btnProfile.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToProfileFragment())
            }
        }
    }

    private fun loadMeme() {
        if (memeList.size % 7 == 0) {
            viewModel.getMeme()
        }
    }

    private fun initViewPager() {
        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewpager.adapter = memeAdapter

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var prevPosition = 0
            override fun onPageSelected(position: Int) {
                if (prevPosition < position && memeList.size - binding.viewpager.currentItem <= 3) {
                    loadMeme()
                }
                prevPosition = position
            }
        })
    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey check out this meme ${memeList[binding.viewpager.currentItem].url}"
        )
        val chooser = Intent.createChooser(intent, "Complete action using...")
        startActivity(chooser)
    }

    private fun addToFav() {
        val currentMeme = memeList[binding.viewpager.currentItem]
        if (addedToFavourites.contains(currentMeme.url)) {
            Toast.makeText(requireContext(), "already added", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.addToFav(currentMeme.url, currentMeme.title)

        Toast.makeText(requireContext(), "added to favourite", Toast.LENGTH_SHORT).show()
        addedToFavourites.add(currentMeme.url)
    }

    private fun saveImage(bitmap: Bitmap) {

        val currentMeme = memeList[binding.viewpager.currentItem]

        if (Build.VERSION.SDK_INT < 29 && !hasWriteExternalStoragePerm()) {
            /*
            * storing the image bitmap to a global variable so that if perm granted it could save the image by providing bitmap
            * from the global variable
            * */
            requestPermission{
                saveImage(bitmap)
            }
            return
        }

        ImageSaver.saveImage(requireContext(),bitmap,currentMeme.title )
    }

    private fun hasWriteExternalStoragePerm() =
        requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


    override fun onDestroyView() {
        binding.viewpager.adapter = null
        _binding = null
        super.onDestroyView()
    }

}