package com.example.n_meme.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favourites::class], version = 1)
abstract class FavDataBase : RoomDatabase(){
    abstract fun favDao() : FavDao
}