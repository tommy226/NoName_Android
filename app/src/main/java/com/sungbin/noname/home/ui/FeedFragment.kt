package com.sungbin.noname.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.databinding.FragmentFeedBinding
import com.sungbin.noname.home.viewmodel.SharedViewModel

class FeedFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

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

        // Inflate the layout for this fragment
        return binding.root
    }

}