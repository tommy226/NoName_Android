package com.sungbin.noname.home.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.R
import com.sungbin.noname.databinding.FragmentFeedBinding
import com.sungbin.noname.databinding.ItemBoardBinding
import com.sungbin.noname.detail.ui.DetailActivity
import com.sungbin.noname.detail.ui.DetailMyActivity
import com.sungbin.noname.home.adapter.FeedAdapter
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.profile.ui.OtherProfileActivity
import com.sungbin.noname.util.EventObserver
import com.sungbin.noname.util.LinearLayoutManagerWrapper
import com.sungbin.noname.util.showToast

class FeedFragment : Fragment() {
    private val TAG = FeedFragment::class.java.simpleName
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentFeedBinding
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)

        binding.run {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.feedRecycler.apply {
            feedAdapter = FeedAdapter(viewModel)
            feedAdapter.setOnItemClickLister(onClickListner)
            feedAdapter.items = mutableListOf()
            adapter = feedAdapter
            layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.feedResponse.observe(viewLifecycleOwner, Observer { feed ->
            Handler(Looper.getMainLooper()).postDelayed({
                feedAdapter.setList(feed.items.boards.toMutableList())
                feedAdapter.notifyDataSetChanged()
            }, 1000)
        })

        viewModel.feedClear.observe(viewLifecycleOwner, EventObserver{ isClear ->
            if(isClear){
                feedAdapter.items.clear()
                feedAdapter.notifyDataSetChanged()
            }
        })

        binding.feedRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {             // 데이터 없을 시 무한 스크롤 현상이 일어나서 스크롤이 움직일 시 활성화
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                    // 스크롤이 끝에 도달했는지 확인
                    if (!binding.feedRecycler.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                        feedAdapter.deleteLoading()
                        viewModel.getBoards(++viewModel.page)
                    }
                }
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    val onClickListner: FeedAdapter.OnItemClickListener = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(v: View, data: Board) {
            when (v.id) {
                R.id.feed_profileImage, R.id.feed_profileNickname -> {
                    if (data.memberDto.id == viewModel.myId) {     // 유저 아이디 검증
                        (activity as HomeActivity).transFragment(ProfileFragment())
                    } else {
                        val intent = Intent(v.context, OtherProfileActivity::class.java)
                        intent.putExtra("memberId", data.memberDto.id)
                        startActivity(intent)
                    }
                }
                R.id.feed_heart, R.id.feed_fill_heart -> {                  //  좋아요 클릭 시 상세 뷰어로 이동
                    val intent =
                        if (data.memberDto.id == viewModel.myId) {
                            Intent(v.context, DetailMyActivity::class.java)
                        } else {
                            Intent(v.context, DetailActivity::class.java)
                        }
                    intent.putExtra("boardId", data.id)
                    startActivity(intent)
                }
                R.id.feed_comment -> {
                    v.context.showToast("댓글")
                }
                R.id.feed_share -> {
                    v.context.showToast("공유")
                }
                else -> {
                }
            }
        }
    }
}