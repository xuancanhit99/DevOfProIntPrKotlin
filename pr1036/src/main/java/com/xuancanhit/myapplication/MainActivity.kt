package com.xuancanhit.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

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
}