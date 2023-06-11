package com.example.n_meme.ui.home

import androidx.lifecycle.*
import com.example.n_meme.data.api.MemeApiService
import com.example.n_meme.data.model.MemeResponse
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.Favourites
import com.example.n_meme.data.repository.Repository
import com.example.n_meme.util.ApiResponse
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: FavDao,
    private val repository: Repository
): ViewModel() {
    val memeResponseLiveData: LiveData<ApiResponse<MemeResponse>>
        get() = repository.memeResponseLiveData

    fun getMeme(category: String){
        viewModelScope.launch {
            repository.getMeme(category)
        }
    }

    fun addToFav(favUrl : String,memeTitle:String){
        viewModelScope.launch {
            dao.insertFav(Favourites(0,favUrl,memeTitle))
        }
    }

}