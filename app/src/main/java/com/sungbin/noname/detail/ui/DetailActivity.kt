package com.sungbin.noname.detail.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityDetailBinding
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.detail.viewmodel.DetailViewModel
import com.sungbin.noname.home.ui.HomeActivity
import com.sungbin.noname.home.viewmodel.SharedViewModel

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
        viewModel.boardId = this.boardId
        viewModel.getBoardDetail(boardId)
    }
}