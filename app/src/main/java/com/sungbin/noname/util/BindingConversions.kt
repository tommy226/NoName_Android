package com.sungbin.noname.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingConversions {
    @BindingAdapter("imageUrl","error")
    @JvmStatic
    fun loadImage(imageView : ImageView, url : String, error : Drawable){

        Glide.with(imageView.context)
            .load(url)
            .error(error)
            .into(imageView)
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadLocalImage(imageView : ImageView, uri : Uri){

        Glide.with(imageView.context)
            .load(uri)
            .centerCrop()
            .into(imageView)
    }
}