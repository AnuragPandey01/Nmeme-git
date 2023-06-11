package com.example.n_meme.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.n_meme.data.api.MemeApiService
import com.example.n_meme.data.model.MemeResponse
import com.example.n_meme.util.ApiResponse
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: MemeApiService) {

    private val _memeResponseLiveData = MutableLiveData<ApiResponse<MemeResponse>>()
    val memeResponseLiveData: LiveData<ApiResponse<MemeResponse>>
        get() = _memeResponseLiveData

    suspend fun getMeme(category: String){
        try{
            _memeResponseLiveData.postValue(ApiResponse.Loading())
            val response = apiService.getMeme(category)
            handleResponse(response)
        }catch (e:IOException){
            _memeResponseLiveData.postValue(ApiResponse.Error("No Internet Connection"))
        }catch (e:Exception){
            _memeResponseLiveData.postValue(ApiResponse.Error("Something Went Wrong"))
        }

    }

    private fun handleResponse(response: Response<MemeResponse>) {
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            _memeResponseLiveData.postValue(ApiResponse.Success(body))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _memeResponseLiveData.postValue(ApiResponse.Error(errorObj.getString("message")))
        }
        else{
            _memeResponseLiveData.postValue(ApiResponse.Error("Something Went Wrong"))
        }
    }
}