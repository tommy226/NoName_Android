package com.sungbin.noname.util

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.sungbin.noname.R
import com.sungbin.noname.detail.ui.DetailActivity
import com.sungbin.noname.home.adapter.FeedImageAdapter
import com.sungbin.noname.home.adapter.MemberFeedAdapter
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.FileDto
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

object BindingConversions {
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(imageView : ImageView, url : String){
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .error(R.drawable.ic_baseline_face_24)
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

    @BindingAdapter("app:imageAdapter","app:setIndicator")
    @JvmStatic
    fun ViewPager2.setImageAdapter(fileDtos: List<FileDto>, wormDotsIndicator: WormDotsIndicator){
        val feedImageAdapter = FeedImageAdapter()

        this.adapter = feedImageAdapter
        feedImageAdapter.images = fileDtos.toMutableList()
        feedImageAdapter.notifyDataSetChanged()
        wormDotsIndicator.setViewPager2(this)
    }

    @BindingAdapter("app:userBoardList")
    @JvmStatic
    fun RecyclerView.userBoardList(boardList: List<Board>){
        val memberFeedAdapter = MemberFeedAdapter()

        memberFeedAdapter.setOnItemClickLister(object : MemberFeedAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: Board) {
                val intent = Intent(v.context, DetailActivity::class.java)
                intent.putExtra("boardId", data.id)
                v.context.startActivity(intent)
            }
        })

        this.apply {
            adapter = memberFeedAdapter
            layoutManager = GridLayoutManager(this.context, 3)
        }
        memberFeedAdapter.items = boardList.toMutableList()
        this.scrollToPosition(memberFeedAdapter.itemCount - 15)
        memberFeedAdapter.notifyDataSetChanged()
    }
}