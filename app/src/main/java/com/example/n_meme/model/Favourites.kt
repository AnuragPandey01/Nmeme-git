package com.example.n_meme.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Favourites (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val url :String
)