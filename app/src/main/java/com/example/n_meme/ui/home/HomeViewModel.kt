package com.example.n_meme.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.example.n_meme.api.RetrofitInstance
import com.example.n_meme.model.MemeResponse
import com.example.n_meme.model.database.FavDao
import com.example.n_meme.model.database.FavDataBase
import com.example.n_meme.model.database.Favourites
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private var dao: FavDao
    init {
        dao = FavDataBase.getDatabaseInstance(application.applicationContext).favDao()
    }
    private var _response : MutableLiveData<Response<MemeResponse>> = MutableLiveData()

    val response : LiveData<Response<MemeResponse>>
        get() = _response

    fun getMeme(category: String){
        viewModelScope.launch {
             _response.value = RetrofitInstance.api.getMeme(category)
        }
    }

    fun addToFav(favUrl : String,memeTitle:String){
        viewModelScope.launch {
            dao.insertFav(Favourites(0,favUrl,memeTitle))
        }
    }

}