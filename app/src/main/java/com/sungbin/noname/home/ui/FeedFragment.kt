package com.sungbin.noname.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.databinding.FragmentFeedBinding
import com.sungbin.noname.home.adapter.FeedAdapter
import com.sungbin.noname.home.data.FeedDataTemp
import com.sungbin.noname.home.viewmodel.FeedViewModel

class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by activityViewModels()

    private lateinit var binding: FragmentFeedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)

        binding.run {
            vm = viewModel
            lifecycleOwner =this@FeedFragment
        }

        val items = mutableListOf<FeedDataTemp>()
        items.add(
            FeedDataTemp(
                "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
                "문성빈",
                getString(R.string.long_text)
            )
        )
        items.add(
            FeedDataTemp(
                "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                "홍길동",
                getString(R.string.long_text)
            )
        )
        items.add(
            FeedDataTemp(
                "https://cdn.pixabay.com/photo/2020/09/02/18/03/girl-5539094_1280.jpg",
                "김배배",
                getString(R.string.long_text)
            )
        )

        val adapter = FeedAdapter()
        adapter.items = items
        binding.feedRecycler.adapter = adapter
        binding.feedRecycler.layoutManager = LinearLayoutManager(activity)


        // Inflate the layout for this fragment
        return binding.root
    }



}