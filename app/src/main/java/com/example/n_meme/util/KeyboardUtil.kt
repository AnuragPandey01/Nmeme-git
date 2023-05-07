package com.example.n_meme.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun Context.hideKeyboard(editText: EditText){
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(editText.windowToken,0)
}

fun Context.showToast(msg : String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}