package com.xuancanhit.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Communicator {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fl_main_color_list_fragment1, ColorListFragment())
        fragmentTransaction.add(R.id.fl_main_colored_fragment2, ColoredFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun passDataComm(colorPosition: Int) {
        val bundle = Bundle()
        bundle.putInt("COLOR", colorPosition)

        val coloredFragment = ColoredFragment()
        coloredFragment.arguments = bundle

        val fragmentTransactionOnClick = fragmentManager.beginTransaction()
        fragmentTransactionOnClick.replace(R.id.fl_main_colored_fragment2, coloredFragment)
        fragmentTransactionOnClick.addToBackStack(null)
        fragmentTransactionOnClick.commit()
    }
}