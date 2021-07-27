package com.sungbin.noname.home.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.home.viewmodel.HomeViewModel
import com.sungbin.noname.login.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class HomeActivity : AppCompatActivity(), CoroutineScope {
    private val TAG = HomeActivity::class.java.simpleName

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val viewmmodel: HomeViewModel by viewModels()

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()

        binding.apply {
            vm = viewmmodel
            lifecycleOwner = this@HomeActivity
        }

        setSwipeRefresh()

//        setSupportActionBar(binding.homeToolbar)
//        supportActionBar?.apply {
//            setDisplayShowCustomEnabled(true)
//            setDisplayShowTitleEnabled(false)
//        }

        binding.homeToolbar.setNavigationOnClickListener {
            Log.d("OpenDrawer", "Open")
            if (!binding.homeDrawerlayout.isDrawerOpen(GravityCompat.START)) {
                binding.homeDrawerlayout.openDrawer(GravityCompat.START)
            }
        }

        setDrawer()
    }

    private fun setSwipeRefresh() {
        binding.swipe.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimaryDark))
        binding.swipe.setOnRefreshListener {
            val intent = Intent(this,HomeActivity::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
            finish()

            binding.swipe.isRefreshing = false
        }
    }

    private fun setDrawer(){
        // 드로어 레이아웃 슬라이드 잠금 여부 설정
        binding.homeDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


    }

    override fun onBackPressed() {
        if (binding.homeDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.homeDrawerlayout.closeDrawers()
        } else {
            super.onBackPressed()
            ActivityCompat.finishAffinity(this)
        }
    }
}