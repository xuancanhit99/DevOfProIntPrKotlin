package com.xuancanhit.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class ColoredFragment : Fragment() {

    private var valuePos:Int? = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_colored, container, false)
        valuePos = arguments?.getInt("COLOR")

        val colorNamesList = resources.getStringArray(R.array.colorNames)
        val colorValuesList = resources.getIntArray(R.array.colorValues)
        for (i in colorNamesList.indices) {
            if (valuePos == i) {
                rootView.setBackgroundColor(colorValuesList[i])
                break
            }
        }
        return rootView
    }
}