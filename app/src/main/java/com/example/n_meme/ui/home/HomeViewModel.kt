package com.example.n_meme.ui.home

import androidx.lifecycle.*
import com.example.n_meme.data.api.MemeApiService
import com.example.n_meme.data.model.MemeResponse
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.Favourites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: FavDao,
    private val memeApiService: MemeApiService
): ViewModel() {

    private var _response : MutableLiveData<Response<MemeResponse>> = MutableLiveData()

    val response : LiveData<Response<MemeResponse>>
        get() = _response

    fun getMeme(category: String){
        viewModelScope.launch {
             _response.value = memeApiService.getMeme(category)
        }
    }

    fun addToFav(favUrl : String,memeTitle:String){
        viewModelScope.launch {
            dao.insertFav(Favourites(0,favUrl,memeTitle))
        }
    }

}