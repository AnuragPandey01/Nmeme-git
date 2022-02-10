package com.example.n_meme.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavDao {

    @Insert
    suspend fun insertFav(favourites: Favourites)

    @Delete
    suspend fun deleteFav(favourites: Favourites)

    @Query("SELECT * FROM favourites")
    fun getFav() : LiveData<List<Favourites>>
}