package com.example.n_meme.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.Favourites
import com.example.n_meme.data.local.PreferenceManager
import com.example.n_meme.data.model.MemeResponse
import com.example.n_meme.data.repository.Repository
import com.example.n_meme.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: FavDao,
    private val repository: Repository,
    private val preferenceManager: PreferenceManager
): ViewModel() {
    val memeResponseLiveData: LiveData<ApiResponse<MemeResponse>>
        get() = repository.memeResponseLiveData

    fun getMeme(){
        viewModelScope.launch {
            repository.getMeme(preferenceManager.getSubreddit()?:"memes")
        }
    }

    fun addToFav(favUrl : String,memeTitle:String){
        viewModelScope.launch {
            dao.insertFav(Favourites(0,favUrl,memeTitle))
        }
    }

}