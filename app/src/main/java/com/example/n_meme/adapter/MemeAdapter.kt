package com.example.n_meme.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.n_meme.R
import com.example.n_meme.model.Meme


class MemeAdapter(val context: Context,val memeList: List<Meme>): RecyclerView.Adapter<MemeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.meme_item_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currMeme = memeList[position]

         Glide.with(context)
            .load(currMeme.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.placeholder)
            .thumbnail(
                Glide.with(context)
                    .load(currMeme.preview[1])
            )
             .listener(object : RequestListener<Drawable>{
                 override fun onLoadFailed(
                     e: GlideException?,
                     model: Any?,
                     target: Target<Drawable>?,
                     isFirstResource: Boolean
                 ): Boolean {
                       holder.img.setImageResource(R.drawable.failed_placeholder)
                     return false
                 }

                 override fun onResourceReady(
                     resource: Drawable?,
                     model: Any?,
                     target: Target<Drawable>?,
                     dataSource: DataSource?,
                     isFirstResource: Boolean
                 ): Boolean {
                     return false
                 }

             })
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return memeList.size
    }

    inner class ViewHolder( itemView:View):RecyclerView.ViewHolder(itemView){
        val img: ImageView = itemView.findViewById(R.id.meme_img)
    }
}
