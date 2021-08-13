package com.sungbin.noname.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sungbin.noname.R
import com.sungbin.noname.databinding.FragmentFeedBinding
import com.sungbin.noname.home.adapter.FeedAdapter
import com.sungbin.noname.home.data.FeedDataTemp
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.profile.ui.OtherProfileActivity
import com.sungbin.noname.util.showToast

class FeedFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    val items = mutableListOf<FeedDataTemp>()

    private lateinit var binding: FragmentFeedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)

        binding.run {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        for(i in 1..10){
            items.add(
                FeedDataTemp(
                    "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
                    "문성빈$i",
                    getString(R.string.long_text)
                )
            )
            items.add(
                FeedDataTemp(
                    "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                    "홍길동$i",
                    getString(R.string.long_text)
                )
            )
            items.add(
                FeedDataTemp(
                    "https://cdn.pixabay.com/photo/2020/09/02/18/03/girl-5539094_1280.jpg",
                    "김배배$i",
                    getString(R.string.long_text)
                )
            )
        }




        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBoard()

        val adapter = FeedAdapter()
        adapter.items = items
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickLister(onClickListner)
        binding.feedRecycler.adapter = adapter
        binding.feedRecycler.layoutManager = LinearLayoutManager(activity)
    }

    val onClickListner: FeedAdapter.OnItemClickListener = object : FeedAdapter.OnItemClickListener{
        override fun onItemClick(v: View, data: FeedDataTemp) {
            if (data.name == viewModel.myName.value) {              // 임시
                (activity as HomeActivity).transFragment(ProfileFragment())
            } else {
                val intent = Intent(v.context, OtherProfileActivity::class.java)
                startActivity(intent)
            }
        }

    }
}