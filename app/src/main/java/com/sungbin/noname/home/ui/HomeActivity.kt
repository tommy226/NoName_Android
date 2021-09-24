package com.sungbin.noname.home.ui

import android.app.Activity
import androidx.fragment.app.Fragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.login.ui.LoginActivity
import com.sungbin.noname.upload.ui.UploadActivity
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.IntentKey
import com.sungbin.noname.util.PreferenceUtil
import com.sungbin.noname.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class HomeActivity : AppCompatActivity(), CoroutineScope {
    private val TAG = HomeActivity::class.java.simpleName
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val viewModel: SharedViewModel by viewModels()

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "lifecycle-> onCreate")
        super.onCreate(savedInstanceState)

        job = Job()

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@HomeActivity
        }

        setSwipeRefresh()
        setDrawer()
        setBottomNavi()

        transFragment(FeedFragment())

        viewModel.getInfo(viewModel.myId) // 자기 정보 가져오기
        viewModel.getBoards(viewModel.page) // 게시물 첫 페이지 요청
        viewModel.getSubscribeOwnerId(viewModel.myId) // 팔로워 정보 받아오기
    }

    override fun onStart() {
        Log.d(TAG, "lifecycle-> onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "lifecycle-> onResume")
        super.onResume()
    }

    private fun setSwipeRefresh() {
        binding.apply {
            swipe.setColorSchemeColors(ContextCompat.getColor(this@HomeActivity, R.color.colorPrimaryDark))
            swipe.setOnRefreshListener {
                val intent = Intent(this@HomeActivity, HomeActivity::class.java)
                overridePendingTransition(0, 0)
                startActivity(intent)
                finish()

                swipe.isRefreshing = false
            }
        }
    }

    private fun setDrawer(){
        binding.apply {
            // 햄버거 버튼
            titleMenu.setOnClickListener {
                Log.d("OpenDrawer", "Open")
                if (!binding.homeDrawerlayout.isDrawerOpen(GravityCompat.START)) {
                    binding.homeDrawerlayout.openDrawer(GravityCompat.START)
                }
            }

            // 업로드 fab
            fabUpload.setOnClickListener {
                val intent = Intent(this@HomeActivity, UploadActivity::class.java)
                startForResult.launch(intent)
            }

            // 드로어 레이아웃 슬라이드 잠금 여부 설정
            homeDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            // 네비게이션 뷰 클릭 리스너
            homeNavigation.closeNaviImg.setOnClickListener { closeDrawer() }
            homeNavigation.layoutProfile.setOnClickListener {
                homeBottomNavi.selectedItemId = R.id.menu_my
                closeDrawer()
            }
            homeNavigation.naviMyinfo.setOnClickListener {
                closeDrawer()
            }
            homeNavigation.naviLogout.setOnClickListener {
                App.prefs.remove()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            homeNavigation.naviSetting.setOnClickListener {

            }
        }
    }
    private fun closeDrawer() = binding.homeDrawerlayout.closeDrawers()
    private fun setBottomNavi(){
        binding.homeBottomNavi.run {
            setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.menu_feed -> transFragment(FeedFragment())
                    R.id.menu_search -> transFragment(SearchFragment())
                    R.id.menu_favorite -> transFragment(FavoriteFragment())
                    else -> transFragment(ProfileFragment())
                }
                true
            }
        }
    }

    fun transFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_container, fragment)
            .commit()

        val title: String = when(fragment){
            is FeedFragment -> getString(R.string.title_feed)
            is SearchFragment -> getString(R.string.title_search)
            is FavoriteFragment -> getString(R.string.title_favorite)
            else-> getString(R.string.title_profile)
        }
        viewModel.titleName.value = title
    }

    var time: Long = 0
    override fun onBackPressed() {
        when {
            binding.homeDrawerlayout.isDrawerOpen(GravityCompat.START) -> {
                closeDrawer()
            }
            System.currentTimeMillis() - time >= 2000 -> {
                time = System.currentTimeMillis();
                showToast("한번 더 누르면 종료 됩니다.")
            }
            else -> {
                super.onBackPressed()
                ActivityCompat.finishAffinity(this)
            }
        }
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_hold, R.anim.slide_out_right)
    }

    override fun onPause() {
        Log.d(TAG, "lifecycle-> onPause")
        super.onPause()
    }

    override fun onRestart() {
        refresh()
        Log.d(TAG, "lifecycle-> onRestart")
        super.onRestart()
    }

    override fun onStop() {
        Log.d(TAG, "lifecycle-> onStop")
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "lifecycle-> onDestroy")
        job.cancel()
    }

    private fun refresh(){
        viewModel.getInfo(viewModel.myId)  // 프로필 재요청

        viewModel.feedClear.value = Event(true) // 피드 데이터 삭제 후 재요청
        viewModel.page = 0
        viewModel.getBoards(viewModel.page)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra(IntentKey.RESULT)
                Log.d(TAG, "registerForActivityResult intent data : $data")

                when (data) {
                    "upload" -> {
                        refresh()
                    }
                }

            }
        }
}