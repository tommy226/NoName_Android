package com.sungbin.noname.detail.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityDetailBinding
import com.sungbin.noname.databinding.ActivityDetailMyBinding
import com.sungbin.noname.detail.viewmodel.DetailViewModel
import com.sungbin.noname.upload.ui.UploadActivity
import com.sungbin.noname.upload.ui.UploadEditActivity
import com.sungbin.noname.util.EventObserver
import com.sungbin.noname.util.IntentKey
import com.sungbin.noname.util.showToast

class DetailMyActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.java.simpleName

    private val viewModel: DetailViewModel by viewModels()

    private val binding: ActivityDetailMyBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_detail_my)
    }

    private val boardId: Int by lazy {
        intent.getIntExtra("boardId",-1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@DetailMyActivity
            indicator = binding.dotsIndicator
            boardCancel.setOnClickListener { onBackPressed() }
        }
        setSupportActionBar(binding.detailMyToolbar)
        likedButton()

        viewModel.boardId = this.boardId
        viewModel.getBoardDetail(boardId)
        viewModel.getlikeCount(boardId)

        viewModel.isLiked.observe(this, EventObserver { isLiked ->
            if (isLiked) {
                Log.d(TAG, "ISLIKED ? $isLiked")                    // 요청 성공 시 좋아요 버튼 뷰 변경
                binding.detailMyHeart.visibility = View.INVISIBLE
                binding.detailMyFillHeart.visibility = View.VISIBLE
            } else {
                Log.d(TAG, "ISLIKED ? $isLiked")
                binding.detailMyHeart.visibility = View.VISIBLE
                binding.detailMyFillHeart.visibility = View.INVISIBLE
            }
        })

        viewModel.isDelete.observe(this, EventObserver{ isDeleted ->
            if(isDeleted){
                finish()
                showToast("글이 삭제 되었어요")
            }
        })

        viewModel.isDetailLikes.observe(this, EventObserver{ isDetailLikes ->
            if(isDetailLikes){
                val intent = Intent(this, DetailSubscribeActivty::class.java)
                intent.putExtra("boardId", boardId)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteMyBoard(boardId)
            }
            R.id.menu_config -> {
                val intent = Intent(this@DetailMyActivity, UploadEditActivity::class.java)
                intent.putExtra("boardId", boardId)
                intent.putExtra("name", viewModel.detailResponse.value?.memberDto?.name)
                intent.putExtra("image", viewModel.detailResponse.value?.memberDto?.src)
                intent.putExtra("content", viewModel.detailResponse.value?.content)
                startForResult.launch(intent)
            }
            else -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun likedButton(){
        binding.detailMyHeart.setOnClickListener {
            viewModel.boardId?.let { boardId -> viewModel.likeBoard(boardId) }
        }
        binding.detailMyFillHeart.setOnClickListener {
            viewModel.detailResponse.value?.fallow?.id?.let { subscribeId -> viewModel.unlikeBoard(subscribeId) }
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra(IntentKey.RESULT)
                Log.d(TAG, "registerForActivityResult intent data : $data")

                when (data) {       // 글 수정 성공 시
                    "edit" -> {
                        viewModel.getBoardDetail(boardId)
                        viewModel.getlikeCount(boardId)
                    }
                }

            }
        }
}