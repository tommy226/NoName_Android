package com.sungbin.noname.upload.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityUploadEditBinding
import com.sungbin.noname.detail.ui.DetailMyActivity
import com.sungbin.noname.home.ui.HomeActivity
import com.sungbin.noname.upload.viewmodel.UploadEditViewModel
import com.sungbin.noname.util.EventObserver
import com.sungbin.noname.util.IntentKey
import com.sungbin.noname.util.showToast

class UploadEditActivity : AppCompatActivity() {
    private val TAG = UploadEditActivity::class.java.simpleName

    private val viewModel: UploadEditViewModel by viewModels()

    private val binding: ActivityUploadEditBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_upload_edit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            vm = viewModel
            activity = this@UploadEditActivity
            lifecycleOwner = this@UploadEditActivity
        }
        val intent = intent

        viewModel.boardId = intent.getIntExtra("boardId", -1)
        viewModel.myName.value = intent.getStringExtra("name")
        viewModel.myImageUrl.value = intent.getStringExtra("image")
        viewModel.inputContent.value = intent.getStringExtra("content")

        viewModel.isEdit.observe(this, EventObserver{ isEdit ->
            if(isEdit){
                showToast("글이 수정 되었어요")
                val intent = Intent(this, DetailMyActivity::class.java).apply {
                    putExtra(IntentKey.RESULT, "edit")
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}