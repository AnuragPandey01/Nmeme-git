package com.example.n_meme.ui.home.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.n_meme.R
import com.example.n_meme.model.Meme


class MemeAdapter : RecyclerView.Adapter<MemeAdapter.ViewHolder>() {

    private val _memeList: MutableList<Meme> = mutableListOf()
    val memeList: List<Meme>
        get() = _memeList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.meme_item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.img.context
        val currMeme = _memeList[position]

        holder.title.text = currMeme.title
        Glide.with(context)
            .asBitmap()
            .load(currMeme.url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.placeholder)
            .thumbnail(
                Glide.with(context)
                    .asBitmap()
                    .load(currMeme.preview[1])
            )
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.img.setImageResource(R.drawable.failed_placeholder)
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return _memeList.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _memeList.clear()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.meme_img)
        val title: TextView = itemView.findViewById(R.id.meme_title)
    }

    fun setData(list: List<Meme>) {
        _memeList.addAll(list)
        notifyItemRangeChanged(_memeList.size, 7)
    }
}
