package com.xuancanhit.sims.ui.activities.student


import android.R.attr.typeface
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.tool.nav.*
import com.xuancanhit.sims.ui.fragments.student.ChatFragment
import com.xuancanhit.sims.ui.fragments.student.HomeFragment
import com.xuancanhit.sims.ui.fragments.student.LearningResultsFragment
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_student_main.*
import kotlinx.android.synthetic.main.menu_left_drawer.*


class StudentMainActivity : AppCompatActivity(), OnItemSelectedListener,
    PassDataFragmentAndActivity {

    //CODE for nav
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

    private var idFragmentOnView: Int = 0 //Variable id fragment on view right now

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var student: Student? = null

    //private var viewMenu: View? = null // View from fragment (click tu menu item fragment vd: tv_Name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        //Initialize firebase
        auth = Firebase.auth
        database = Firebase.database.reference

        //Hide status bar
        MainActivity.hideStatusBar(window)

        //Init Nav
        initNavBottomAndTopLeft(savedInstanceState)
    }


    private fun initNavBottomAndTopLeft(savedInstanceState: Bundle?) {
        //----------------Setup nav top-left----------------
        setSupportActionBar(toolbar_stu)
        //registerForContextMenu(toolbar_stu)
        //toolbar_stu.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_person_toolbar)
        supportActionBar?.title = null
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
        //Select Listener top-left nav
        adapter.setListener(this)


        //Set up list nav top-left
        val list = findViewById<RecyclerView>(R.id.rv_stu_nav_list)
        list.isNestedScrollingEnabled = false
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        //----------------End Setup nav top-left----------------

        //---------------------Setup Bottom Nav------------------
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
        //---------------------End Setup Bottom Nav------------------

        //Set first fragment Home
        supportFragmentManager.beginTransaction()
            .add(R.id.body_container, HomeFragment())
            .commit()
    }


    //Event item top-left nav selected
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
            CODE_LOGOUT -> signOutAccount()
        }
        slidingRootNav.closeMenu()

        if (selectedScreen != null) {
            showFragment(selectedScreen)
        }
    }


    //Show Fragment
    private fun showFragment(fragment: Fragment) {
        // set you initial fragment object
        supportFragmentManager.beginTransaction()
            .replace(R.id.body_container, fragment)
            //.addToBackStack(null)
            .commit()
    }

//    //-------------------Menu for toolbar--------------------
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_toolbar, menu)
//
//        //---Custom font menu item----
//        for (i in 0 until menu!!.size()) {
//            val mi = menu.getItem(i)
//            //for applying a font to subMenu ...
//            val subMenu = mi.subMenu
//            if (subMenu != null && subMenu.size() > 0) {
//                for (j in 0 until subMenu.size()) {
//                    val subMenuItem = subMenu.getItem(j)
//                    applyFontToMenuItem(subMenuItem)
//                }
//            }
//            //the method we have create in activity
//            applyFontToMenuItem(mi)
//        }
//        //---Custom font menu item----
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_toolbar_profile -> {
//                Toast.makeText(this, "Profile Selected", Toast.LENGTH_SHORT).show()
//                true
//            }
//            R.id.menu_toolbar_change_password -> {
//                Toast.makeText(this, "Change Password Selected", Toast.LENGTH_SHORT).show()
//                true
//            }
//            R.id.menu_toolbar_logout -> {
//                Toast.makeText(this, "Logout Selected", Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//    //-------------------End Menu for toolbar--------------------

    //-----------------Menu for fragment--------------------------

//    //Custom font menu item
//    private fun applyFontToMenuItem(mi: MenuItem) {
//        val font = Typeface.create(ResourcesCompat.getFont(this, R.font.my_font), Typeface.NORMAL)
//        val mNewTitle = SpannableString(mi.title)
//        mNewTitle.setSpan(
//            TypefaceSpan(font),
//            0,
//            mNewTitle.length,
//            Spannable.SPAN_INCLUSIVE_INCLUSIVE
//        )
//        mi.title = mNewTitle
//    }
//
//    override fun onCreateContextMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//
//        //Get view from Text View in Fragment profile choose Vd: tv_Name
//        viewMenu = v
//        super.onCreateContextMenu(menu, v, menuInfo)
//        menuInflater.inflate(R.menu.menu_student_text, menu)
//
//        //---Custom font menu item----
//        for (i in 0 until menu!!.size()) {
//            val mi = menu.getItem(i)
//            //for applying a font to subMenu ...
//            val subMenu = mi.subMenu
//            if (subMenu != null && subMenu.size() > 0) {
//                for (j in 0 until subMenu.size()) {
//                    val subMenuItem = subMenu.getItem(j)
//                    applyFontToMenuItem(subMenuItem)
//                }
//            }
//            //the method we have create in activity
//            applyFontToMenuItem(mi)
//        }
//        //---Custom font menu item----
//    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_stu_text_edit -> {
//                showDialogTextInputAndPassDataToTextViewFragment()
//                true
//            }
//            R.id.menu_stu_text_delete -> {
//                Toast.makeText(this, "Delete Selected", Toast.LENGTH_SHORT).show()
//                true
//            }
//            R.id.menu_stu_text_exit -> {
//                Toast.makeText(this, "Exit Selected", Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//
//
//    private fun showDialogTextInputAndPassDataToTextViewFragment() {
//        //Dialog Are you sure
//        val input = EditText(this)
//
//        //Get id TextView From Fragment Profile and get text show input: EditText
//        val tv = findViewById<TextView>(viewMenu!!.id)
//        input.setText(tv.text.toString())
//        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this, R.style.MyTitle_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
//        materialAlertDialogBuilder.setTitle(MainActivity.textWithMyFont(this,"Please enter data", R.font.my_font, Typeface.BOLD))
//            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
//                // Respond to negative button press
//                dialog.cancel()
//            }
//            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
//                // Respond to positive button press
//
//                // Set text to textview(Fragment)
//                tv.text = input.text.toString()
//            }
//            .setView(input)
//            .show()
//    }
//
//    //-----------------End Menu for fragment--------------------------

    //Set color top-left nav
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

    //Load title top-left nav
    private fun loadScreenTitles(): Array<String> {
        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }

    //Load icon top-left nav
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





    //Set location top-nav title and bottom-nav icon be right
    override fun setNavState(id: Int) {
        idFragmentOnView = id
        bottom_navigation_student.menu.findItem(idFragmentOnView).isChecked = true
        when (idFragmentOnView) {
            R.id.nav_home -> {
                supportActionBar?.title =
                    MainActivity.textWithMyFont(this,getString(R.string.home), R.font.my_font, Typeface.BOLD)
                adapter.setChecked(CODE_HOME)
            }
            R.id.nav_profile -> {
                supportActionBar?.title =
                    MainActivity.textWithMyFont(this,getString(R.string.my_profile), R.font.my_font, Typeface.BOLD)
                adapter.setChecked(CODE_PROFILE)
            }
            R.id.nav_learning_results -> {
                supportActionBar?.title = MainActivity.textWithMyFont(this,
                    getString(R.string.learning_results),
                    R.font.my_font,
                    Typeface.BOLD
                )
                adapter.setChecked(CODE_RESULTS)
            }
            R.id.nav_chat -> {
                supportActionBar?.title =
                    MainActivity.textWithMyFont(this,getString(R.string.chat), R.font.my_font, Typeface.BOLD)
                adapter.setChecked(CODE_CHAT)
            }
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction()
            .add(R.id.body_container, HomeFragment())
            .commit()

        //Set double back allow exit app
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        //Back button when double click and view on fragment home
        if (idFragmentOnView == R.id.nav_home) {
            slidingRootNav.closeMenu()
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }


    //Sign out
    private fun signOutAccount() {

        //Dialog Are you sure
        MaterialAlertDialogBuilder(
            this,
            R.style.MyTitle_ThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle(resources.getString(R.string.title_dialog_logout))
            .setMessage(resources.getString(R.string.message_are_you_sure))
//            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
//                // Respond to neutral button press
//
//            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                // Respond to positive button press
                auth.signOut()
                startActivity(Intent(this, StudentLoginActivity::class.java))
                finish()
            }
            .show()
    }


    //Reload UI
    private fun reload(name: String = "", email: String = "", img: String = "") {
        tv_stu_nav_name.text = name
        tv_stu_nav_email.text = email
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(iv_stu_nav)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload(
                currentUser.displayName.toString(),
                currentUser.email.toString(),
                currentUser.photoUrl.toString()
            )

            database.child("Students").child(currentUser.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        student = snapshot.getValue(Student::class.java)
                        student?.let {
                            reload(it.name.toString(), it.email.toString(), it.img.toString())
                            tv_stu_nav_no.text = it.no
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }
}