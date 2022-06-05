package com.xuancanhit.sims.ui.interfaces

import android.view.View

interface PassDataFragmentAndActivity {

    //Pass data from fragment to activity
    //Let Activity what fragment is on View -> set checked in bottom nav
    fun setNavState(id: Int)

    //Send view(fragment) to activity
    fun getViewFragment(view: View, nameView: String)

    fun passDataComm(uid: String)


}