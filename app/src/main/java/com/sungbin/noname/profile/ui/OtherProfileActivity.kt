package com.sungbin.noname.profile.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityOtherProfileBinding
import com.sungbin.noname.detail.ui.DetailActivity
import com.sungbin.noname.detail.ui.DetailMyActivity
import com.sungbin.noname.detail.ui.DetailSubscribeActivty
import com.sungbin.noname.profile.viewmodel.OtherProfileViewModel
import com.sungbin.noname.util.EventObserver
import com.sungbin.noname.util.PreferenceUtil

class OtherProfileActivity : AppCompatActivity() {
    private val TAG = OtherProfileActivity::class.java.simpleName

    private val viewModel: OtherProfileViewModel by viewModels()

    private val binding: ActivityOtherProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_other_profile)
    }
    private val memberId: Int by lazy {
        intent.getIntExtra("memberId",-1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@OtherProfileActivity
            backBtn.setOnClickListener { onBackPressed() }
            otherFollower.setOnClickListener{ getDetailFallow()}
            followerCount.setOnClickListener { getDetailFallow() }
        }
        fallowButton()
        viewModel.clearBoards()  // 처음 데이터 삭제
        viewModel.getInfo(memberId) // 다른 사람 정보 가져오기
        viewModel.fallowOtherInfo(memberId) // 팔로우 정보


        var page = 0
        viewModel.getBoardsOther(memberId, page)

        viewModel.isFallowed.observe(this, EventObserver { isFallowed ->
            if (isFallowed) {
                Log.d(TAG, "ISFALLOWED ? $isFallowed")
                binding.otherFallowBtn.visibility = View.INVISIBLE
                binding.otherUnfallowBtn.visibility = View.VISIBLE
            } else {
                Log.d(TAG, "ISFALLOWED ? $isFallowed")
                binding.otherFallowBtn.visibility = View.VISIBLE
                binding.otherUnfallowBtn.visibility = View.INVISIBLE
            }
        })

        binding.otherBoardRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(dy > 0){                         // 데이터 없을 시 무한 스크롤 현상이 일어나서 스크롤이 움직일 시 활성화
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                    // 스크롤이 끝에 도달했는지 확인
                    if (!binding.otherBoardRecycler.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                        viewModel.getBoardsOther(memberId,++page)
                    }
                }
            }
        })
    }
    fun getDetailFallow(){
        val intent = Intent(this, DetailSubscribeActivty::class.java)
        intent.putExtra("ownerId", memberId)
        startActivity(intent)
    }

    fun fallowButton(){
        binding.otherFallowBtn.setOnClickListener {
            viewModel.fallowOther(memberId)
        }
        binding.otherUnfallowBtn.setOnClickListener {
            viewModel.memberInfo.value?.items?.member?.fallow?.id?.let { subscribeId -> viewModel.unfallowOther(subscribeId) }
        }
    }
}