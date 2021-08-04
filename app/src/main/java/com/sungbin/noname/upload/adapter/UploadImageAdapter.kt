package com.sungbin.noname.upload.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.databinding.ItemUploadimageBinding
import com.sungbin.noname.util.createThumnail

class UploadImageAdapter(val uriList: List<Uri>) : RecyclerView.Adapter<UploadImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UploadImageAdapter.ViewHolder {
        val binding = ItemUploadimageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadImageAdapter.ViewHolder, position: Int) {
        val uri = uriList[position]
        holder.bind(uri)
    }

    override fun getItemCount(): Int {
        return uriList.size
    }


    inner class ViewHolder(val binding: ItemUploadimageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(uri: Uri){
            binding.uri = uri
        }
    }

}