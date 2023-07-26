package com.example.n_meme.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.io.OutputStream

object ImageSaver {

    private val RELATIVE_PATH = Environment.DIRECTORY_PICTURES + File.separator + "Nmeme"

    fun saveImage(context: Context,bitmap: Bitmap,fileName:String) {

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            // without this part causes "Failed to create new MediaStore record" exception to be invoked (uri is null below)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, RELATIVE_PATH)
            }
        }
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var outputStream: OutputStream? = null
        try {

            /*
            * creating an outputStream
            * stream : streams are the sequence of data that are read from the source and written to the destination
            * An input stream is used to read data from the source. And, an output stream is used to write data to the destination.
            */

            val resolver = context.contentResolver

            val uri = resolver.insert(contentUri, contentValues)
                ?: throw IOException("Failed to create new MediaStore record.")

            outputStream = resolver.openOutputStream(uri)
                ?: throw IOException("Failed to create output stream")

            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 95, outputStream)) {
                throw IOException("Failed to save bitmap.")
            }
            Toast.makeText(context, "saved image to Gallery", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
    }

}
