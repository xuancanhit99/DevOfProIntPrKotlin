package com.xuancanhit.sims.ui.interfaces

import android.view.View

interface ItemClickListener {
    fun onClick(view: View?, position: Int, isLongClick: Boolean)
}