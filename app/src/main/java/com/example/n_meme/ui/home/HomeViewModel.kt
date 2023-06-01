package com.example.n_meme.ui.home

import androidx.lifecycle.*
import com.example.n_meme.data.api.MemeApiService
import com.example.n_meme.data.model.MemeResponse
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.Favourites
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: FavDao,
    private val memeApiService: MemeApiService,
    private val mixpanelAPI: MixpanelAPI
): ViewModel() {

    private var _response : MutableLiveData<Response<MemeResponse>> = MutableLiveData()

    val response : LiveData<Response<MemeResponse>>
        get() = _response

    fun getMeme(category: String){
        viewModelScope.launch {

            val response = memeApiService.getMeme(category)
            if(response.isSuccessful){
                _response.value = response
            }else{
                logToMixPanel("memeApi failed response",JSONObject().apply{
                    put("raw",response.raw().toString())
                })
            }
        }
    }

    fun addToFav(favUrl : String,memeTitle:String){
        viewModelScope.launch {
            dao.insertFav(Favourites(0,favUrl,memeTitle))
        }
    }

    private fun logToMixPanel(eventName: String,jsonObject: JSONObject? = null){
        mixpanelAPI.track(eventName,jsonObject)
    }

    override fun onCleared() {
        super.onCleared()
        mixpanelAPI.flush()
    }

}