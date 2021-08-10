package com.sungbin.noname.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemBoardBinding
import com.sungbin.noname.home.data.FeedDataTemp

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var items = mutableListOf<FeedDataTemp>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root){
        val binding = binding
        fun bind(data: FeedDataTemp){
            binding.data = data
        }
    }

}