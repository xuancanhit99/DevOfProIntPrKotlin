package com.xuancanhit.sims.ui.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xuancanhit.sims.R
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity


class AdminHomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_ad_home)
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

}