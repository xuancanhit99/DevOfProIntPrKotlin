package com.xuancanhit.sims.ui.activities.student

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.nav.*
import com.xuancanhit.sims.ui.fragments.student.ChatFragment
import com.xuancanhit.sims.ui.fragments.student.HomeFragment
import com.xuancanhit.sims.ui.fragments.student.LearningResultsFragment
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder

import kotlinx.android.synthetic.main.activity_student_main.*
import java.util.*

class StudentMainActivity : AppCompatActivity(), OnItemSelectedListener {

    companion object {
        const val CODE_HOME = 0
        const val CODE_PROFILE = 1
        const val CODE_RESULTS = 2
        const val CODE_CHAT = 3
        const val CODE_LOGOUT = 5
    }

    private lateinit var screenTitles: Array<String>
    private lateinit var screenIcons: Array<Drawable?>
    private lateinit var slidingRootNav: SlidingRootNav
    private lateinit var adapter:DrawerAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        initBottomNav()

        //Top Nav
        setSupportActionBar(toolbar_stu)
        slidingRootNav = SlidingRootNavBuilder(this)
            .withToolbarMenuToggle(toolbar_stu)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()


        screenTitles = loadScreenTitles()
        screenIcons = loadScreenIcons()


        adapter = DrawerAdapter(
            Arrays.asList(
                createItemFor(CODE_HOME).setChecked(
                    true
                ),
                createItemFor(CODE_PROFILE),
                createItemFor(CODE_RESULTS),
                createItemFor(CODE_CHAT),
                (SpaceItem(48) as DrawerItem<DrawerAdapter.ViewHolder>),
                createItemFor(CODE_LOGOUT)
            )
        )
        adapter.setListener(this)
        val list = findViewById<RecyclerView>(R.id.rv_stu_nav_list)
        list.isNestedScrollingEnabled = false
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        adapter.setSelected(CODE_HOME)
    }

    private fun initBottomNav() {
        //Bottom-Left Nav
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //fragmentTransaction.replace(R.id.body_container, HomeFragment())
        //fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        bottom_navigation_student.selectedItemId = R.id.nav_home

        bottom_navigation_student.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null
            when(item.itemId) {
                R.id.nav_home -> {
                    fragment = HomeFragment()
                    //adapter.setSelected(CODE_HOME)
                }
                R.id.nav_profile -> {
                    fragment = ProfileFragment()
                    //adapter.setSelected(CODE_PROFILE)
                }

                R.id.nav_learning_results -> {
                    fragment = LearningResultsFragment()
                    //adapter.setSelected(CODE_RESULTS)
                }
                R.id.nav_chat -> {
                    fragment = ChatFragment()
                    //adapter.setSelected(CODE_CHAT)
                }
            }
            val fragmentTransactionOn = supportFragmentManager.beginTransaction()
            //fragmentTransactionOn.replace(R.id.body_container, fragment!!)
            fragmentTransactionOn.addToBackStack(null)
            fragmentTransactionOn.commit()
            true
        }
    }

    override fun onItemSelected(position: Int) {

        var selectedScreen: Fragment?=null
        when(position) {
            CODE_HOME -> selectedScreen = HomeFragment()
            CODE_PROFILE -> selectedScreen = ProfileFragment()
            CODE_RESULTS -> selectedScreen = LearningResultsFragment()
            CODE_CHAT-> selectedScreen = ChatFragment()
            CODE_LOGOUT -> finish()
        }
        slidingRootNav.closeMenu()

        if (selectedScreen != null) {
            showFragment(selectedScreen)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.body_container, fragment).addToBackStack(null)
            .commit()
    }

    private fun createItemFor(position: Int): DrawerItem<DrawerAdapter.ViewHolder> {
        var simpleItem: SimpleItem? = null
        screenIcons[position]?.let { simpleItem = SimpleItem(it, screenTitles[position])
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
    
}