package com.sungbin.noname.upload.ui

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityUploadBinding
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.upload.adapter.UploadImageAdapter
import com.sungbin.noname.upload.viewmodel.UploadViewModel
import com.sungbin.noname.util.*
import gun0912.tedimagepicker.builder.TedImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class UploadActivity : AppCompatActivity() {
    private val TAG = UploadActivity::class.java.simpleName

    private val viewmodel: UploadViewModel by viewModels()

    private val binding: ActivityUploadBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_upload)
    }

    private lateinit var adapter: UploadImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            vm = viewmodel
            activity = this@UploadActivity
            lifecycleOwner = this@UploadActivity
        }
        viewmodel.getInfo()

        viewmodel.toast.observe(this, EventObserver { message ->
            showToast(message)
        })

        viewmodel.boardId.observe(this, Observer {  idResponse ->
            val id = idResponse.items.board.id
            uploadimages(id = id)
        })
    }


    fun permissionCheck(){
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                imagePick()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
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

    private fun imagePick() {
        TedImagePicker.with(this@UploadActivity)
            .startMultiImage { uriList ->
                adapter = UploadImageAdapter(uriList)
                binding.imageRecyclerview.adapter = adapter
                binding.imageRecyclerview.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                for (i in uriList.indices) {
                    Log.d(TAG, "File path : ${FileUtils.getPath(this, uriList[i])}")
                }

                viewmodel.selectedImages.let { list ->
                    if(list.isNotEmpty()) list.clear()
                    list.addAll(uriList)
                }
            }
    }

    private fun uploadimages(id: String) {
        val files = mutableListOf<MultipartBody.Part>()
        for (i in viewmodel.selectedImages.indices) {
            val filepath = FileUtils.getPath(this, viewmodel.selectedImages[i])
            val multipartBody =
                FileUtils.multipartBody(filepath = filepath.toString(), key = "files")
            files.add(multipartBody)
        }

        viewmodel.uploadFiles(files, id)   // 게시글 이미지 업로드
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

