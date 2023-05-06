package com.example.n_meme.data.api

import com.example.n_meme.data.model.RedditResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MemeApiInterface {

    @GET("{subreddit}/{sort}.json?limit=7")
    suspend fun getMeme(

        @Path("subreddit")
        subreddit: String ,

        @Path("sort")
        sortType: String

    ): RedditResponse


    @GET("{subreddit}/{sort}.json?limit=7")
    suspend fun getMeme(

        @Path("subreddit")
        subreddit: String ,

        @Path("sort")
        sortType: String,

        @Query("after")
        after: String

    ): RedditResponse
}