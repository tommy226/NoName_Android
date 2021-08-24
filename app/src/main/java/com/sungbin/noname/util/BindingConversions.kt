package com.sungbin.noname.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.sungbin.noname.R
import com.sungbin.noname.home.adapter.FeedImageAdapter
import com.sungbin.noname.home.adapter.MemberFeedAdapter
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.FileDto

object BindingConversions {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(imageView : ImageView, url : String){
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
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

    @BindingAdapter("app:imageAdapter")
    @JvmStatic
    fun ViewPager2.setImageAdapter(fileDtos: List<FileDto>){
        val feedImageAdapter = FeedImageAdapter()

        feedImageAdapter.images = fileDtos.toMutableList()
        this.adapter = feedImageAdapter
        feedImageAdapter.notifyDataSetChanged()
    }

    @BindingAdapter("app:setFeedList")
    @JvmStatic
    fun RecyclerView.setFeedList(feedList: List<Board>){
        val memberFeedAdapter = MemberFeedAdapter()

        this.adapter = memberFeedAdapter
        this.layoutManager = GridLayoutManager(this.context, 3)

        memberFeedAdapter.items = feedList.toMutableList()
        memberFeedAdapter.notifyDataSetChanged()
    }
}