package com.xuancanhit.sims.tool.nav.bottom

interface FragmentToActivity {

    //Pass data from fragment to activity
    //Let Activity what fragment is on View -> set checked in bottom nav
    fun setNavState(id: Int)
}