package com.sungbin.noname.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemImageBinding
import com.sungbin.noname.home.data.FileDto

class FeedImageAdapter : RecyclerView.Adapter<FeedImageAdapter.ImageViewHolder>() {

    var images = mutableListOf<FileDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val data = images[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val binding = binding
        fun bind(data: FileDto) {
            binding.data = data
            binding.executePendingBindings()
        }
    }


}