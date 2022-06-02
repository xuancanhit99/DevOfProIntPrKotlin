package com.xuancanhit.sims.ui.activities.student

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.xuancanhit.sims.R
import com.xuancanhit.sims.ui.fragments.student.ChatFragment
import com.xuancanhit.sims.ui.fragments.student.HomeFragment
import com.xuancanhit.sims.ui.fragments.student.LearningResultsFragment
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import kotlinx.android.synthetic.main.activity_student_main.*

class StudentMainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initView()
    }

    private fun initView() {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.body_container, HomeFragment())
        //fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        bottom_navigation_student.selectedItemId = R.id.nav_home

        bottom_navigation_student.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null
            when(item.itemId) {
                R.id.nav_home -> fragment = HomeFragment()
                R.id.nav_profile -> fragment = ProfileFragment()
                R.id.nav_learning_results -> fragment = LearningResultsFragment()
                R.id.nav_chat -> fragment = ChatFragment()
            }
            val fragmentTransactionOn = supportFragmentManager.beginTransaction()
            fragmentTransactionOn.replace(R.id.body_container, fragment!!)
            fragmentTransactionOn.addToBackStack(null)
            fragmentTransactionOn.commit()
            true
        }

    }
}