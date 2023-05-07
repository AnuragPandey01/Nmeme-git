package com.example.n_meme.data.model

import com.example.n_meme.data.model.Meme

data class MemeResponse(
    val count: Int,
    val memes: List<Meme>
)