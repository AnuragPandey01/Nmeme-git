package com.example.n_meme.ui.home

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.n_meme.R
import com.example.n_meme.databinding.FragmentFeedBinding
import com.example.n_meme.model.Meme
import com.example.n_meme.ui.home.adapter.MemeAdapter
import java.io.File
import java.io.IOException
import java.io.OutputStream

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding: FragmentFeedBinding
        get() = _binding!!

    private var currentBitmap: Bitmap? = null
    private var addedToFavourites = mutableListOf<String>()
    private val memeAdapter: MemeAdapter by lazy { MemeAdapter() }
    private val memeList: List<Meme> by lazy { memeAdapter.memeList }
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }
    private val args : FeedFragmentArgs by lazy {
        FeedFragmentArgs.fromBundle(requireArguments())
    }
    
    //throws an exception if called before onCreate
    private val category :String by lazy{
        CategoryBrowseFragment.categories[args.categoryIndex].second
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFeedBinding.inflate(layoutInflater)
        setOnClickListener()
        initViewPager()
        loadMeme()
        try {
            viewModel.response.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    memeAdapter.setData(response.body()!!.memes)
                } else {
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Check your connection", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }


    private fun setOnClickListener() {
        binding.apply {

            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_feedFragment_to_categoryBrowseFragment)
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
                            currentBitmap = resource
                            saveImage(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }
        }
    }

    private fun loadMeme() {
        if (memeList.size % 7 == 0) {
            viewModel.getMeme(category)
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

        if (Build.VERSION.SDK_INT < 29 && !hasWriteExternalStoragePerm()) {
            /*
            * storing the image bitmap to a global variable so that if perm granted it could save the image by providing bitmap
            * from the global variable
            * */
            currentBitmap = bitmap
            requestPermission()
            return
        }

        val currentMeme = memeList[binding.viewpager.currentItem]
        val name = "${currentMeme.title} by ${currentMeme.author}"
        val relativePath = Environment.DIRECTORY_PICTURES + File.separator + "Nmeme"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            // without this part causes "Failed to create new MediaStore record" exception to be invoked (uri is null below)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativePath)
            }
        }
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var outputStream: OutputStream? = null
        try {

            /*
            * creating an outputStream
            * stream : streams are the sequence of data that are read from the source and written to the destination
            * An input stream is used to read data from the source. And, an output stream is used to write data to the destination.
            */

            val resolver = requireContext().contentResolver

            val uri = resolver.insert(contentUri, contentValues)
                ?: throw IOException("Failed to create new MediaStore record.")

            outputStream = resolver.openOutputStream(uri)
                ?: throw IOException("Failed to create output stream")

            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 95, outputStream)) {
                throw IOException("Failed to save bitmap.")
            }
            Toast.makeText(requireContext(), "saved image to Gallery", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {

        } finally {
            outputStream?.close()
        }
    }

    private fun hasWriteExternalStoragePerm() =
        requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        //showing rationale if denied many time
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "This permission is needed to store image in external storage", Toast.LENGTH_SHORT).show()
            return
        }
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                saveImage(currentBitmap!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "To save image permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onDestroyView() {
        binding.viewpager.adapter = null
        _binding = null
        super.onDestroyView()
    }

}