package com.example.n_meme.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.n_meme.R
import com.example.n_meme.adapter.MemeAdapter
import com.example.n_meme.model.Meme

open class MemeActivity : AppCompatActivity(){

    val TAG = "MainActivity"
    private  lateinit var adapter: MemeAdapter
    private var memeList = mutableListOf<Meme>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

    }







}