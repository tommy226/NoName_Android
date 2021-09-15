package com.sungbin.noname.detail.ui

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityDetailBinding
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.detail.viewmodel.DetailViewModel
import com.sungbin.noname.home.ui.HomeActivity
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.util.EventObserver

class DetailActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.java.simpleName

    private val viewModel: DetailViewModel by viewModels()

    private val binding: ActivityDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_detail)
    }

    private val boardId: Int by lazy {
        intent.getIntExtra("boardId",-1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@DetailActivity
            indicator = binding.dotsIndicator
            boardCancel.setOnClickListener { onBackPressed() }
        }
        likedButton()

        viewModel.boardId = this.boardId
        viewModel.getBoardDetail(boardId)
        viewModel.getlikeCount(boardId)

        viewModel.isLiked.observe(this, EventObserver { isLiked ->
            if (isLiked) {
                Log.d(TAG, "ISLIKED ? $isLiked")                    // 요청 성공 시 좋아요 버튼 뷰 변경
                binding.detailHeart.visibility = View.INVISIBLE
                binding.detailFillHeart.visibility = View.VISIBLE
            } else {
                Log.d(TAG, "ISLIKED ? $isLiked")
                binding.detailHeart.visibility = View.VISIBLE
                binding.detailFillHeart.visibility = View.INVISIBLE
            }
        })
    }

    fun likedButton(){
        binding.detailHeart.setOnClickListener {
            viewModel.boardId?.let { boardId -> viewModel.likeBoard(boardId) }
        }
        binding.detailFillHeart.setOnClickListener {
            viewModel.detailResponse.value?.fallow?.id?.let { subscribeId -> viewModel.unSubscribe(subscribeId) }
        }
    }
}