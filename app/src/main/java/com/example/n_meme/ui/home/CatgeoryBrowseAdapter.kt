package com.example.n_meme.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.n_meme.R

class CategoryBrowseAdapter(
    private val categories:List<Pair<String,String>>,
    val onItemClick: (itemIndex:Int)-> Unit
): RecyclerView.Adapter<CategoryBrowseAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //getting view value resources
        val context = holder.categoryTitle.context
        val categoryTitle = categories[position].first
        val drawableUri = "@drawable/${categoryTitle.lowercase()}"
        val imageResource = context.resources.getIdentifier(drawableUri,null,context.packageName)

        //setting views
        holder.memeCoverIcon.setImageDrawable(ContextCompat.getDrawable(context,imageResource))
        holder.categoryTitle.text = categoryTitle

        holder.root.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = categories.size


    inner class ViewHolder(private val itemview: View): RecyclerView.ViewHolder(itemview){
        val categoryTitle = itemview.findViewById<TextView>(R.id.category_title)
        val root = itemview.findViewById<CardView>(R.id.root)
        val memeCoverIcon : ImageView = itemView.findViewById(R.id.meme_cover_icon)
    }
}