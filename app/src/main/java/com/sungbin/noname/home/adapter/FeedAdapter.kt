package com.sungbin.noname.home.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemBoardBinding
import com.sungbin.noname.home.data.FeedDataTemp
import com.sungbin.noname.profile.ui.OtherProfileActivity

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var items = mutableListOf<FeedDataTemp>()
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(v: View, data: FeedDataTemp)
    }
    fun setOnItemClickLister(listener: OnItemClickListener){
        this.listener = listener
    }

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

    inner class ViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val binding = binding
        fun bind(data: FeedDataTemp) {
            binding.data = data

            itemView.setOnClickListener {
                listener.onItemClick(binding.root, data)
            }
        }
    }
}