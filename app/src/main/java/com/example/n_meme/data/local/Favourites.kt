package com.example.n_meme.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity is used to create sqlite table . An entity is a data class and defines all the column name and type in the table
@Entity(tableName = "favourites")
data class Favourites (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val url :String,
    val memeTitle: String = " "
)