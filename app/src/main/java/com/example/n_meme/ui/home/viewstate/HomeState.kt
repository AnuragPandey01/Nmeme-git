package com.example.n_meme.ui.home.viewstate

import com.example.n_meme.data.model.Meme

sealed class HomeState {
    object Idle: HomeState()
    object Loading: HomeState()
    data class Response(val data: List<Meme>): HomeState()
    data class Error(val message : String): HomeState()

}