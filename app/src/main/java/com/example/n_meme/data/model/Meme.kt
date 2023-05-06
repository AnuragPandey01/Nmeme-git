package com.example.n_meme.data.model

data class Meme(
    val author: String,
    val nsfw: Boolean,
    val preview: String,
    val spoiler: Boolean,
    val subreddit: String,
    val title: String,
    val ups: Int,
    val url: String
)