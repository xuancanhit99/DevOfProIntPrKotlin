package com.xuancanhit.sims.ui.fragments.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.nav.bottom.FragmentToActivity


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as FragmentToActivity?)!!.setNavState(R.id.nav_home)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}