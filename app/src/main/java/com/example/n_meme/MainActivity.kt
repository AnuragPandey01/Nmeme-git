package com.example.n_meme

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.n_meme.databinding.ActivityMainBinding

open class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var memeUrl: String? = null

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMeme()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.category.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId){
                else -> loadMeme()
            }
        }
        binding.nextMeme.setOnClickListener {
            loadMeme()
        }
        binding.shareMeme.setOnClickListener { shareMeme() }
        binding.addToFav.setOnClickListener { addToFav() }
    }


    private fun loadMeme() {
        binding.memeTitle.text = getString(R.string.loading)
        binding.meme.setImageResource(R.drawable.meme_loading)
        Log.d("tag", "loadMeme:entered ")
        apiCallOnCategory()
    }

    private fun apiCallOnCategory(){
        val queue = Volley.newRequestQueue(this)

        // Request a string response from the provided URL by creating a json class object
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, getApiUrl(), null,
            { response ->

                memeUrl = response.getString("url")
                val memeTitleStore = response.getString("title")
                val previewArray = response.getJSONArray("preview")
                val preview = previewArray[1]
                Glide.with(this)
                    .load(memeUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.meme_loading)
                    .thumbnail(
                        setThumbnail(preview)
                    )
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.meme.setImageResource(R.drawable.meme_failed)
                            return false
                        }
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.memeTitle.text = memeTitleStore
                            return false
                        }
                    })
                    .into(binding.meme)

            },
            { error ->
                val reason =error.message
                binding.meme.setImageResource(R.drawable.meme_failed)
                Toast.makeText(this, "$reason", Toast.LENGTH_SHORT).show()
                binding.memeTitle.text =this.getString(R.string.meme_loading_failed)
            }
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }


    fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey check out this meme $memeUrl")
        val chooser = Intent.createChooser(intent, "Complete action using...")
        startActivity(chooser)
    }

    fun addToFav() {
        Toast.makeText(this, "Added to fav successfully", Toast.LENGTH_SHORT).show()
    }

    private fun getApiUrl(): String {
        return when(binding.category.checkedRadioButtonId){
            R.id.anime -> "https://meme-api.herokuapp.com/gimme/animemes"
            R.id.wholesome -> "https://meme-api.herokuapp.com/gimme/wholesomememes"
            R.id.dank -> "https://meme-api.herokuapp.com/gimme/dankmemes"
            R.id.history -> "https://meme-api.herokuapp.com/gimme/historymemes"
            R.id.gaming -> "https://meme-api.herokuapp.com/gimme/gamingmemes"
            else ->"https://meme-api.herokuapp.com/gimme/memes"
        }
    }

    private fun setThumbnail( thumbnail:Any) : RequestBuilder<Drawable> {
        return Glide.with(this)
            .load(thumbnail)
    }

}



