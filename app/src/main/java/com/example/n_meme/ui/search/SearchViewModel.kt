package com.example.n_meme.ui.search

import androidx.lifecycle.ViewModel
import com.example.n_meme.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    fun saveSubreddit(subreddit: String) {
        preferenceManager.saveSubreddit(subreddit)
    }

}