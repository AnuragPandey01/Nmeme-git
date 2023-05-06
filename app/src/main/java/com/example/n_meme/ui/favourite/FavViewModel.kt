package com.example.n_meme.ui.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.n_meme.data.local.database.FavDao
import com.example.n_meme.data.local.database.FavDataBase
import com.example.n_meme.data.local.database.Favourites
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavViewModel(application: Application): AndroidViewModel(application) {
    private var dao: FavDao

    init {
        dao = FavDataBase.getDatabaseInstance(application.applicationContext).favDao()
    }

     inner class GetAllFav {
        lateinit var favList : LiveData<List<Favourites>>
        init {
            viewModelScope.launch{
                val fav = async { dao.getFav() }
                favList = fav.await()
            }
        }
    }




}