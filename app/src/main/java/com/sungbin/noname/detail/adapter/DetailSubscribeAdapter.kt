package com.sungbin.noname.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemSubscribeMemberBinding
import com.sungbin.noname.home.data.Subscribe

class DetailSubscribeAdapter : RecyclerView.Adapter<DetailSubscribeAdapter.SubScribeViewHolder>() {
    var items = mutableListOf<Subscribe>()

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(v: View, data: Subscribe)
    }
    fun setOnItemClickLister(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):DetailSubscribeAdapter.SubScribeViewHolder {
        val binding = ItemSubscribeMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubScribeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailSubscribeAdapter.SubScribeViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = items.size

    inner class SubScribeViewHolder(binding: ItemSubscribeMemberBinding) : RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun bind(data: Subscribe){
            binding.data = data
            binding.executePendingBindings()
            itemView.setOnClickListener {
                listener.onItemClick(binding.root, data)
            }
        }
    }
}