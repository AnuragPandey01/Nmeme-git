package com.example.n_meme.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

object Share{

    fun asUrl(context: Context,  url: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey!, check out this meme $url"
        )
        val chooser = Intent.createChooser(intent, "Complete action using...")
        startActivity(context,chooser,null)
    }
}