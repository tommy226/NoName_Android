package com.sungbin.noname.profile.ui

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityProfileEditBinding
import com.sungbin.noname.home.viewmodel.ProfileViewModel
import com.sungbin.noname.profile.viewmodel.ProfileEditViewModel
import com.sungbin.noname.util.*
import gun0912.tedimagepicker.builder.TedImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileEditActivity : AppCompatActivity() {
    private val TAG = ProfileEditActivity::class.java.simpleName

    private val viewmodel: ProfileEditViewModel by viewModels()

    private val binding: ActivityProfileEditBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
    }

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            vm = viewmodel
            activity = this@ProfileEditActivity
            lifecycleOwner = this@ProfileEditActivity
        }


        viewmodel.toast.observe(this, EventObserver { message ->
            showToast(message)
        })

        viewmodel.idResponse.observe(this, Observer { idResponse ->
            val id = idResponse.items.id
            imageUpload(id, imageUri)
        })

        viewmodel.profileResult.observe(this, Observer {
            finish()
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

    private fun imagePick(){
        TedImagePicker.with(this)
            .start { uri ->
                Log.d(TAG, uri.toString())
                binding.profileImageView.createThumnail(uri = uri)
                imageUri = uri
            }
    }

    private fun imageUpload(id: String, uri: Uri){
//        val id: RequestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
//        val partsMap = hashMapOf<String, RequestBody>()
//        partsMap["id"] = id

        val filepath = FileUtils.getPath(this, uri)
        val multipartBody = FileUtils.multipartBody(filepath = filepath.toString(), key = "file")

        viewmodel.editImageRequest(multipartBody)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}