package com.example.n_meme.data.api

import com.example.n_meme.data.model.MemeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface MemeApiService {

    @GET("{subReddit}/7")
    suspend fun getMeme(@Path("subReddit") category:String ): Response<MemeResponse>
}