package com.example.n_meme.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

// DAO is an interface which defines all the method to be accessed from the database
@Dao
interface FavDao {

    @Insert
    suspend fun insertFav(favourites: Favourites)

    @Delete
    suspend fun deleteFav(favourites: Favourites)

    @Query("SELECT * FROM favourites")
    fun getFav() : LiveData<List<Favourites>>

}