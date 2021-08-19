package com.sungbin.noname.home.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.noname.R
import com.sungbin.noname.databinding.FragmentFeedBinding
import com.sungbin.noname.home.adapter.FeedAdapter

import com.sungbin.noname.home.data.Feed
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.profile.ui.OtherProfileActivity

class FeedFragment : Fragment() {
    private val TAG = FeedFragment::class.java.simpleName
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentFeedBinding
    private lateinit var feedAdapter: FeedAdapter
    private var page = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)

        binding.run {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.feedRecycler.apply {
            feedAdapter = FeedAdapter()
            feedAdapter.setOnItemClickLister(onClickListner)
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.getBoards(page)

        viewModel.feedResponse.observe(viewLifecycleOwner, Observer { feed ->
            Log.d(TAG, feed.toString())

            feedAdapter.setList(feed)
            feedAdapter.notifyItemRangeInserted((page - 1) * 10, 10)
        })

        binding.feedRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.feedRecycler.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {

                    Log.d(TAG, "새로고침")

                    binding.feedRecycler.postDelayed(
                        {
//                            feedAdapter.deleteLoading()
                            feedAdapter.notifyDataSetChanged()
                        },
                        500
                    )
//                    viewModel.getBoards(++page)
                }
            }
        })
    }

    val onClickListner: FeedAdapter.OnItemClickListener = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(v: View, data: Feed) {
            if (data.board.name == viewModel.myName.value) {              // 임시
                (activity as HomeActivity).transFragment(ProfileFragment())
            } else {
                val intent = Intent(v.context, OtherProfileActivity::class.java)
                startActivity(intent)
            }
        }

    }
}