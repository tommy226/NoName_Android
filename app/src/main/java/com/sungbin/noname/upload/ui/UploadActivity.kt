package com.sungbin.noname.upload.ui

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityUploadBinding
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.upload.adapter.UploadImageAdapter
import com.sungbin.noname.upload.viewmodel.UploadViewModel
import com.sungbin.noname.util.FileUtils
import com.sungbin.noname.util.customEnqueue
import com.sungbin.noname.util.showToast
import gun0912.tedimagepicker.builder.TedImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class UploadActivity : AppCompatActivity() {
    private val TAG = UploadActivity::class.java.simpleName

    private val viewmmodel: UploadViewModel by viewModels()

    private val binding: ActivityUploadBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_upload)
    }

    private lateinit var adapter: UploadImageAdapter

    private var imageUriList = mutableListOf<Uri>()

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

                if (imageUriList.isNotEmpty()) imageUriList.clear()
                imageUriList.addAll(uriList)
            }
    }

    fun uploadBoard() {
        val content = binding.boradText.toString()
        ServerImpl.service.boardContentUpload(App.prefs.getString("access", ""), content)
            .customEnqueue(
                onSuccess = { response ->
                    if(response.code()==200){
                        val id = response.body()?.items?.board?.id              // 게시글 id 받은 후 이미지 업로드
                        id?.let { id -> uploadimages(id) }
                    }
                },
                onError = {},
                onFailure = {}
            )
    }

    private fun uploadimages(id: String) {
        val id: RequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val parts = mutableListOf<MultipartBody.Part>()
        val partsMap = hashMapOf<String, RequestBody>()
        partsMap["id"] = id
        for(i in imageUriList.indices){
            val filepath = FileUtils.getPath(this, imageUriList[i])
            val multipartBody = FileUtils.multipartBody(filepath = filepath.toString(), "files")
            parts.add(multipartBody)
        }

        ServerImpl.service.boardFileUpload(App.prefs.getString("access", ""), parts, partsMap)
            .customEnqueue(
                onSuccess = { response ->
                    if(response.code()==200){
                        showToast("게시글을 작성 하였어요")
                        finish()
                    }
                },
                onError = {},
                onFailure = {
                    showToast("서버와의 연결이 원활하지 않습니다.")
                }
            )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

