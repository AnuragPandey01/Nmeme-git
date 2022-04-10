package com.example.n_meme.api

import com.example.n_meme.model.MemeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface MemeApiInterface {

    @GET("{subReddit}/7")
    suspend fun getMeme(@Path("subReddit") category:String ): Response<MemeResponse>
}