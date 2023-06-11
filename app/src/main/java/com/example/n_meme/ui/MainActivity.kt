package com.example.n_meme.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.n_meme.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}