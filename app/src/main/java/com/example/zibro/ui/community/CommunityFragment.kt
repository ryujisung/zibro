package com.example.zibro.ui.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zibro.R
import com.example.zibro.databinding.FragmentCommunityBinding
import com.example.zibro.databinding.FragmentMyBinding
import com.example.zibro.ui.base.BaseFragment

class CommunityFragment: BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        return view
    }
}