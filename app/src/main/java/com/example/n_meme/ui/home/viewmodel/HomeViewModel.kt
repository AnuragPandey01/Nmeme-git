package com.example.n_meme.ui.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.n_meme.data.api.RetrofitInstance
import com.example.n_meme.data.model.Meme
import com.example.n_meme.data.model.RedditResponse
import com.example.n_meme.data.local.database.FavDao
import com.example.n_meme.data.local.database.FavDataBase
import com.example.n_meme.data.local.database.Favourites
import com.example.n_meme.data.local.sharedPreference.PreferenceManager
import com.example.n_meme.ui.home.intent.HomeIntent
import com.example.n_meme.ui.home.intent.SortType
import com.example.n_meme.ui.home.viewstate.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private var dao: FavDao
    val intent = Channel<HomeIntent>()

    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state : StateFlow<HomeState>
        get() = _state

    private lateinit var after : String
    private var currSortPref : SortType
    private val prefManager: PreferenceManager
    private var currSubredditPref: String

    init {

        dao = FavDataBase.getDatabaseInstance(application.applicationContext).favDao()
        prefManager = PreferenceManager(application.applicationContext)
        currSortPref = SortType.valueOf(prefManager.getSortPref()?.uppercase() ?: "HOT")
        currSubredditPref = prefManager.getSubredditPref() ?: "wholesomememes"
        observeIntent()
    }

    private fun observeIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect{
                when(it){
                    is HomeIntent.ChangeSort ->{
                        currSortPref = it.sortType
                        getMeme(sortType = it.sortType, subreddit = currSubredditPref)
                    }
                    is HomeIntent.SaveMeme ->{

                    }
                    is HomeIntent.AddToFav -> {

                    }
                    is HomeIntent.ShareMeme ->{

                    }
                    is HomeIntent.LoadMeme ->{
                        getMeme(after)
                    }
                    is HomeIntent.ChangeSubreddit ->{
                        prefManager.updateSubredditPref(it.subreddit)
                        getMeme(currSortPref,it.subreddit)
                    }
                }
            }
        }
    }

   /* fun getMeme(category: String){
        viewModelScope.launch {
             _response.value = RetrofitInstance.api.getMeme(category,"new")
        }
    }*/

    private fun getMeme(sortType: SortType,subreddit: String){
        viewModelScope.launch {
            _state.value = HomeState.Loading
            _state.value = try{
               /* RetrofitInstance.api.getMeme("animememes", sortType.name.lowercase()).also {
                    HomeState.Response(redditResponseToMeme(it))
                    after = it.data.after
                }*/
                HomeState.Response(
                    redditResponseToMeme(
                        RetrofitInstance.api.getMeme(subreddit, sortType.name.lowercase())
                    )
                )
            }catch (e : Exception){
                HomeState.Error(e.stackTraceToString())
            }
        }
    }

    private fun getMeme(after: String){
        viewModelScope.launch {
            _state.value = HomeState.Loading
            _state.value = try{
                HomeState.Response(
                    redditResponseToMeme(
                        RetrofitInstance.api.getMeme("animememes", currSortPref.name.lowercase(),after)
                    )
                )
            }catch (e : Exception){
                HomeState.Error(e.stackTraceToString())
            }
        }
    }

    fun addToFav(favUrl : String,memeTitle:String){
        viewModelScope.launch {
            dao.insertFav(Favourites(0,favUrl,memeTitle))
        }
    }

    fun getCurrSubreddit(): String{
        return currSubredditPref
    }
    private fun redditResponseToMeme(rawData: RedditResponse): List<Meme>{
        after = rawData.data.after
        val childrenList = rawData.data.children
        val memeList : MutableList<Meme> = mutableListOf()
        childrenList.forEach {
            if(!it.data.is_video && it.data.is_reddit_media_domain){
                memeList.add(
                    Meme(
                        it.data.author,
                        it.data.over_18,
                        it.data.preview.images[0].source.url,
                        it.data.spoiler,
                        it.data.subreddit,
                        it.data.title,
                        it.data.ups,
                        it.data.url
                    )
                )
            }
        }
        return memeList
    }
}