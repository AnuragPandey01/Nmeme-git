package com.example.n_meme.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val BASE_URL =  "https://www.reddit.com/r/"

object RetrofitInstance{
    val api : MemeApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MemeApiInterface::class.java)
    }
}