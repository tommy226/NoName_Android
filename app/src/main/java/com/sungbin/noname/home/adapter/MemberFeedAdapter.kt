package com.sungbin.noname.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemLoadingBinding
import com.sungbin.noname.databinding.ItemMemberBoardBinding
import com.sungbin.noname.home.data.Board

class MemberFeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = mutableListOf<Board>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemMemberBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberFeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = items[position]
        if (holder is MemberFeedViewHolder) {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class MemberFeedViewHolder(binding: ItemMemberBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding = binding
        fun bind(data: Board) {
            binding.data = data
        }
    }

}