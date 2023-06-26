package com.example.n_meme.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.n_meme.databinding.SearchItemViewBinding


class SearchAdapter(val onClick: (String) -> Unit): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var subredditList: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            onClick(subredditList[position])
        }
        holder.bind(subredditList[position])
    }

    override fun getItemCount(): Int {
        return subredditList.size
    }

    class SearchViewHolder(val binding: SearchItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(subreddit: String) {
            binding.subredditName.text = subreddit
        }
    }

    fun filterList(filterlist: List<String>) {
        subredditList = filterlist
        notifyDataSetChanged()
    }
}
