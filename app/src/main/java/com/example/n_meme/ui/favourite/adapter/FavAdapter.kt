package com.example.n_meme.ui.favourite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.n_meme.R
import com.example.n_meme.model.database.Favourites
import com.example.n_meme.ui.favourite.FavouritesFragmentDirections

class FavAdapter(val context: Context, private val favList: List<Favourites>): RecyclerView.Adapter<FavAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fav_item_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(favList[position].url)
            .placeholder(R.drawable.placeholder)
            .sizeMultiplier(0.5F)
            .into(holder.img)

        holder.img.setOnClickListener {
            val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailFavouriteFragment(position)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img : ImageView= itemView.findViewById(R.id.fav_item)
    }


}