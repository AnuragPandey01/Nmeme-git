package com.example.n_meme.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val BASE_URL =  "https://meme-api.herokuapp.com/gimme/"

object RetrofitInstance{
    val api : MemeApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MemeApiInterface::class.java)
    }
}