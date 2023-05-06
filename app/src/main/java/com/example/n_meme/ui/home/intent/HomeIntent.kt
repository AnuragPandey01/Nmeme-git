package com.example.n_meme.ui.home.intent

sealed class HomeIntent {
    data class ChangeSort(val sortType: SortType): HomeIntent()
    data class ChangeSubreddit(val subreddit: String) : HomeIntent()
    data class SaveMeme(val url :String): HomeIntent()
    data class AddToFav(val url :String) : HomeIntent()
    object ShareMeme : HomeIntent()
    object LoadMeme: HomeIntent()
}

enum class SortType{
    TOP,
    NEW,
    HOT
}