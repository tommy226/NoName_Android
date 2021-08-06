package com.sungbin.noname.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.FragmentProfileBinding
import com.sungbin.noname.databinding.FragmentSearchBinding
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.profile.ui.ProfileEditActivity

class ProfileFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)

        binding.run {
            vm = viewModel
            prefs = App.prefs
            lifecycleOwner =this@ProfileFragment
        }

        binding.profileEditBtn.setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}