package com.sungbin.noname.upload.ui

import android.Manifest
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.annotations.Until
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityUploadBinding
import com.sungbin.noname.upload.viewmodel.UploadViewModel
import com.sungbin.noname.util.showToast
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlin.math.log


class UploadActivity : AppCompatActivity() {
    private val TAG = UploadActivity::class.java.simpleName

    private val viewmmodel: UploadViewModel by viewModels()

    private val binding: ActivityUploadBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_upload)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            vm = viewmmodel
            activity = this@UploadActivity
            lifecycleOwner = this@UploadActivity
        }

    }

    fun permissionCheck(){
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                // 접근허용 시 실행할 코드
//                showToast("권한이 허용 되었습니다.")
                TedImagePicker.with(this@UploadActivity)
                    .startMultiImage { uriList ->

                        for (i in 0 until uriList.size ){

                            Log.d(TAG, "file path : ${uriList[i].path.toString()}")
                        }
                    }
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                // 접근거부 시 실행할 코드
                showToast("권한이 거부 되었습니다.")
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage(R.string.permission_denied)
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }
}