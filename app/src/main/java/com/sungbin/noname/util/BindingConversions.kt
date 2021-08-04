package com.sungbin.noname.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sungbin.noname.R

object BindingConversions {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(imageView : ImageView, url : String){
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.baseline_close_white_24)
            .into(imageView)
    }

    @BindingAdapter("app:imageUri")
    @JvmStatic
    fun loadLocalImage(imageView : ImageView, uri : Uri){
        Glide.with(imageView.context)
            .load(uri)
            .centerCrop()
            .error(R.drawable.baseline_close_white_24)
            .into(imageView)
    }
}