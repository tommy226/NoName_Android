package com.sungbin.noname.profile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityOtherProfileBinding
import com.sungbin.noname.profile.viewmodel.OtherProfileViewModel

class OtherProfileActivity : AppCompatActivity() {
    private val TAG = OtherProfileActivity::class.java.simpleName

    private val viewModel: OtherProfileViewModel by viewModels()

    private val binding: ActivityOtherProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_other_profile)
    }
    private val memberId: Int by lazy {
        intent.getIntExtra("memberId",99999)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@OtherProfileActivity
            backBtn.setOnClickListener { onBackPressed() }
        }
        viewModel.clearBoards()  // 처음 데이터 삭제
        viewModel.getInfo(memberId) // 다른 사람 정보 가져오기

        var page = 0
        viewModel.getBoardsOther(memberId, page)
    }
}