package com.example.n_meme.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.n_meme.data.local.FavDao
import com.example.n_meme.data.local.Favourites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    val dao: FavDao,
): ViewModel() {
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