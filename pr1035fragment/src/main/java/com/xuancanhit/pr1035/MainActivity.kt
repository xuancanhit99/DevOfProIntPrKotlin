package com.xuancanhit.pr1035

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private lateinit var btnCaption: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val fragmentTransaction = fragmentManager.beginTransaction()

        var check = true

        fragmentTransaction.add(R.id.fl_main_fragment1, FirstFragment())
        fragmentTransaction.add(R.id.fl_main_fragment2, SecondFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        btnCaption = findViewById(R.id.btn_main_caption)
        btnCaption.setOnClickListener {
            val fragmentTransactionOnClick = fragmentManager.beginTransaction()
            check = if (check) {
                fragmentTransactionOnClick.replace(R.id.fl_main_fragment1, SecondFragment())
                fragmentTransactionOnClick.replace(R.id.fl_main_fragment2, FirstFragment())
                false
            } else {
                fragmentTransactionOnClick.replace(R.id.fl_main_fragment1, FirstFragment())
                fragmentTransactionOnClick.replace(R.id.fl_main_fragment2, SecondFragment())
                true
            }
            fragmentTransactionOnClick.addToBackStack(null)
            fragmentTransactionOnClick.commit()
        }
    }
}

