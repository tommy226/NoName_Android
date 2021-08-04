package com.sungbin.noname.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.sungbin.noname.R
import com.sungbin.noname.databinding.FragmentFeedBinding
import com.sungbin.noname.databinding.FragmentSearchBinding
import com.sungbin.noname.home.viewmodel.SharedViewModel

class SearchFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)

        binding.run {
            vm = viewModel
            lifecycleOwner =this@SearchFragment
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}