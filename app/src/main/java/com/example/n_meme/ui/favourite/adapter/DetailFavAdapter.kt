package com.example.n_meme.ui.favourite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.n_meme.R
import com.example.n_meme.data.local.Favourites

class DetailFavAdapter(val context: Context,private val favList:List<Favourites>):RecyclerView.Adapter<DetailFavAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_fav_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       Glide.with(holder.img.context)
           .load(favList[position].url)
           .placeholder(R.drawable.placeholder)
           .into(holder.img)


    }

    override fun getItemCount(): Int {
        return favList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.meme_img)
    }
}