package com.example.n_meme.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val BASE_URL =  "https://meme-api.herokuapp.com/gimme/"
class RetrofitInstance {

    object GetRetrofitInstance{

        val MemeInstance: MemeApiInterface
        init {
            val retrofitInstance: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            MemeInstance = retrofitInstance.create(MemeApiInterface::class.java)
        }
    }
}