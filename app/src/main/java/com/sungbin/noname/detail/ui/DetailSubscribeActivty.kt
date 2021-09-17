package com.sungbin.noname.detail.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityDetailSubscribeActivtyBinding
import com.sungbin.noname.detail.viewmodel.DetailViewModel

class DetailSubscribeActivty : AppCompatActivity() {
    private val TAG = DetailSubscribeActivty::class.java.simpleName

    private val viewModel: DetailViewModel by viewModels()

    private val binding: ActivityDetailSubscribeActivtyBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_detail_subscribe_activty)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            vm = viewModel
            lifecycleOwner = this@DetailSubscribeActivty
        }
    }
}