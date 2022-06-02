package com.xuancanhit.sims.ui.activities.student

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.nav.bottom.FragmentToActivity
import com.xuancanhit.sims.tool.nav.top.*
import com.xuancanhit.sims.ui.fragments.student.ChatFragment
import com.xuancanhit.sims.ui.fragments.student.HomeFragment
import com.xuancanhit.sims.ui.fragments.student.LearningResultsFragment
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_student_main.*

class StudentMainActivity : AppCompatActivity(), OnItemSelectedListener, FragmentToActivity {

    companion object {
        const val CODE_HOME = 0
        const val CODE_PROFILE = 1
        const val CODE_RESULTS = 2
        const val CODE_CHAT = 3
        const val CODE_LOGOUT = 5
    }

    private var doubleBackToExitPressedOnce = false

    private lateinit var screenTitles: Array<String>
    private lateinit var screenIcons: Array<Drawable?>
    private lateinit var slidingRootNav: SlidingRootNav
    private lateinit var adapter: DrawerAdapter
    private var idFragmentOnView: Int = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        MainActivity.hideStatusBar(window)

        initNavBottomAndTopLeft(savedInstanceState)

    }

    private fun initNavBottomAndTopLeft(savedInstanceState: Bundle?) {

        //Setup nav top-left
        setSupportActionBar(toolbar_stu)
        slidingRootNav = SlidingRootNavBuilder(this)
            .withToolbarMenuToggle(toolbar_stu)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()

        //Load title and icon item nav top-left
        screenTitles = loadScreenTitles()
        screenIcons = loadScreenIcons()

        adapter = DrawerAdapter(
            listOf(
                //Create item on nav and set checked first item
                createItemFor(CODE_HOME).setChecked(true),
                createItemFor(CODE_PROFILE),
                createItemFor(CODE_RESULTS),
                createItemFor(CODE_CHAT),
                (SpaceItem(48) as DrawerItem<DrawerAdapter.ViewHolder>),
                createItemFor(CODE_LOGOUT)
            )
        )

        //Select Listener
        adapter.setListener(this)

        //Set up list nav top-left
        val list = findViewById<RecyclerView>(R.id.rv_stu_nav_list)
        list.isNestedScrollingEnabled = false
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        //Set first fragment Home
        supportFragmentManager.beginTransaction()
            .add(R.id.body_container, HomeFragment())
            .commit()

        //-------------------------
        //Bottom Nav
        bottom_navigation_student.selectedItemId = R.id.nav_home



        bottom_navigation_student.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> adapter.setSelected(CODE_HOME)
                R.id.nav_profile -> adapter.setSelected(CODE_PROFILE)
                R.id.nav_learning_results -> adapter.setSelected(CODE_RESULTS)
                R.id.nav_chat -> adapter.setSelected(CODE_CHAT)
            }
            true
        }
    }

    override fun onItemSelected(position: Int) {
        var selectedScreen: Fragment? = null
        when (position) {
            CODE_HOME -> {
                selectedScreen = HomeFragment()
                //bottom_navigation_student.menu.findItem(R.id.nav_home).isChecked = true
            }
            CODE_PROFILE -> {
                selectedScreen = ProfileFragment()
                //bottom_navigation_student.menu.findItem(R.id.nav_profile).isChecked = true
            }
            CODE_RESULTS -> {
                selectedScreen = LearningResultsFragment()
                //bottom_navigation_student.menu.findItem(R.id.nav_learning_results).isChecked = true
            }
            CODE_CHAT -> {
                selectedScreen = ChatFragment()
                //bottom_navigation_student.menu.findItem(R.id.nav_chat).isChecked = true
            }
            CODE_LOGOUT -> finish()
        }

        slidingRootNav.closeMenu()

        if (selectedScreen != null) {
            showFragment(selectedScreen)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.body_container, fragment)
            //.addToBackStack(null)
            .commit()
    }

    private fun createItemFor(position: Int): DrawerItem<DrawerAdapter.ViewHolder> {
        var simpleItem: SimpleItem? = null
        screenIcons[position]?.let {
            simpleItem = SimpleItem(it, screenTitles[position])
                .withIconTint(color(R.color.light_white))
                .withTextTint(color(R.color.light_white))
                .withSelectedIconTint(color(R.color.bl))
                .withSelectedTextTint(color(R.color.bl))
        }
        return (simpleItem as DrawerItem<DrawerAdapter.ViewHolder>)
    }

    private fun loadScreenTitles(): Array<String> {
        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }

    private fun loadScreenIcons(): Array<Drawable?> {
        val ta = resources.obtainTypedArray(R.array.ld_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
    }

    override fun setNavState(id: Int) {
        idFragmentOnView = id
        bottom_navigation_student.menu.findItem(idFragmentOnView).isChecked = true
        when (idFragmentOnView) {
            R.id.nav_home -> adapter.setChecked(CODE_HOME)
            R.id.nav_profile -> adapter.setChecked(CODE_PROFILE)
            R.id.nav_learning_results -> adapter.setChecked(CODE_RESULTS)
            R.id.nav_chat -> adapter.setChecked(CODE_CHAT)
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction()
            .add(R.id.body_container, HomeFragment())
            .commit()

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        if(idFragmentOnView == R.id.nav_home) {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}