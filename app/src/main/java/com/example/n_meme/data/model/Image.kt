package com.example.n_meme.data.model

data class Image(
    val id: String,
    val resolutions: List<Resolution>,
    val source: Source,
)