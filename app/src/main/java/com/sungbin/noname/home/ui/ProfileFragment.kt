package com.sungbin.noname.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.R
import com.sungbin.noname.databinding.FragmentProfileBinding
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.profile.ui.ProfileEditActivity

class ProfileFragment : Fragment() {
    private val TAG = ProfileFragment::class.java.simpleName

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)

        binding.run {
            vm = viewModel
            fragment = this@ProfileFragment
            lifecycleOwner = viewLifecycleOwner
        }
        viewModel.clearBoards()  // 처음 데이터 삭제

        var page = 0
        viewModel.getBoardsMember(viewModel.myId,page)

        binding.profileBoardRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(dy > 0){                         // 데이터 없을 시 무한 스크롤 현상이 일어나서 스크롤이 움직일 시 활성화
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                    Log.d(TAG, "lastVisibleItemPosition : $lastVisibleItemPosition")
                    Log.d(TAG, "itemTotalCount : $itemTotalCount")

                    // 스크롤이 끝에 도달했는지 확인
                    if (!binding.profileBoardRecycler.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                        viewModel.getBoardsMember(viewModel.myId,++page)
                    }
                }
            }
        })

        return binding.root
    }

    fun editProfile(){
        val intent = Intent(activity, ProfileEditActivity::class.java)
        startActivity(intent)
    }
}