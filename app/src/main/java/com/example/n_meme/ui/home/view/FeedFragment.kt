package com.example.n_meme.ui.home.view

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.sqlite.db.SupportSQLiteCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.n_meme.R
import com.example.n_meme.data.local.sharedPreference.PreferenceManager
import com.example.n_meme.data.model.Meme
import com.example.n_meme.databinding.FragmentFeedBinding
import com.example.n_meme.ui.base.BaseFragment
import com.example.n_meme.ui.home.adapter.MemeAdapter
import com.example.n_meme.ui.home.intent.HomeIntent
import com.example.n_meme.ui.home.intent.SortType
import com.example.n_meme.ui.home.viewmodel.HomeViewModel
import com.example.n_meme.ui.home.viewstate.HomeState
import com.example.n_meme.util.ImageSaver
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

class FeedFragment : BaseFragment(){

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

        initRadioGrp()
        setOnClickListener()
        initViewPager()
        observeState()
        binding.tvCategory.text = viewModel.getCurrSubreddit()
        return binding.root
    }

    private fun initRadioGrp() {
        val sortPref = PreferenceManager(requireContext()).getSortPref()
        when(sortPref){
            "top" -> binding.radioGrp.check(R.id.radio_top)
            "hot" -> binding.radioGrp.check(R.id.radio_hot)
            "new" -> binding.radioGrp.check(R.id.radio_new)
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is HomeState.Idle -> {}
                    is HomeState.Loading ->{
                        binding.animLoading.visibility = View.VISIBLE
                    }
                    is HomeState.Response -> {
                        binding.animLoading.visibility = View.GONE
                        memeAdapter.addData(it.data)
                    }
                    is HomeState.Error -> {
                        binding.animLoading.visibility = View.GONE
                        Log.d("respect", "observeState: ${it.message}")
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun setOnClickListener() {

        binding.apply {
            radioGrp.setOnCheckedChangeListener { _, checkedId ->
                memeAdapter.resetData()
                sendIntentOnPref(checkedId)
            }

            tvCategory.setOnClickListener {
              showTextInputDialog { dialog, textInputLayout, textInputEditText ->
                  val subreddit = textInputEditText.text.toString()
                  if(subreddit.isNotBlank()){
                      Log.d("bitches", "showDialog: dude $subreddit")
                      lifecycleScope.launch {
                          memeAdapter.resetData()
                          viewModel.intent.send(HomeIntent.ChangeSubreddit(subreddit))
                      }
                      tvCategory.text = subreddit
                      dialog.dismiss()
                  }
                  textInputLayout.error = "subreddit cant be empty"
              }
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
        }
    }

    private fun sendIntentOnPref(checkedId: Int){
        when(checkedId){

            R.id.radio_new ->{
                lifecycleScope.launch {
                    viewModel.intent.send(HomeIntent.ChangeSort(SortType.NEW))
                }
            }
            R.id.radio_hot ->{
                lifecycleScope.launch {
                    viewModel.intent.send(HomeIntent.ChangeSort(SortType.HOT))
                }
            }
            R.id.radio_top -> {
                lifecycleScope.launch {
                    viewModel.intent.send(HomeIntent.ChangeSort(SortType.TOP))
                }
            }
        }
    }

    private fun initViewPager() {
        binding.viewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewpager.adapter = memeAdapter

        sendIntentOnPref(binding.radioGrp.checkedRadioButtonId)
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var prevPosition = 0
            override fun onPageSelected(position: Int) {
                if (prevPosition < position && memeList.size - binding.viewpager.currentItem <= 3) {
                    lifecycleScope.launch {
                        viewModel.intent.send(HomeIntent.LoadMeme)
                    }
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

    private fun showTextInputDialog(onOkClicked : (AlertDialog,TextInputLayout,TextInputEditText)-> Unit){

        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.edit_text_dialog,null)
        val editText = view.findViewById<TextInputEditText>(R.id.edit_text)
        val editTextLayout = view.findViewById<TextInputLayout>(R.id.subreddit_input_layout)
        val btnOk: Button = view.findViewById(R.id.btn_ok)
        val btnCancel: Button = view.findViewById(R.id.btn_cancel)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Enter subreddit name")
            .setView(view).create()

        btnOk.setOnClickListener {
            /*val subreddit = editText.text.toString()
            if(subreddit.isNotBlank()){
                Log.d("bitches", "showDialog: dude ${editText.text.toString()}")
                dialog.dismiss()
            }
            editTextLayout.error = "subreddit cant be empty"*/
            onOkClicked(dialog,editTextLayout,editText)
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}



