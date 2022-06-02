package com.xuancanhit.sims.ui.fragments.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.nav.bottom.FragmentToActivity


class ChatFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as FragmentToActivity?)!!.setNavState(R.id.nav_chat)
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }



}