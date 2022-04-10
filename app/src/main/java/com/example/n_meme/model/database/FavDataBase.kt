package com.example.n_meme.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favourites::class], version = 1)
abstract class FavDataBase : RoomDatabase(){

    abstract fun favDao() : FavDao

    companion object{
        @Volatile
        private var INSTANCE : FavDataBase? = null

        fun getDatabaseInstance(context: Context) : FavDataBase {
            synchronized(this){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, FavDataBase::class.java, "favDB").build()
                }
            }
            return INSTANCE!!
        }
    }

}