package com.xuancanhit.sims.tool.nav.top

import android.view.ViewGroup


abstract class DrawerItem<T : DrawerAdapter.ViewHolder> {
    var isChecked = false
    abstract fun createViewHolder(parent: ViewGroup): T
    abstract fun bindViewHolder(holder: T)
    fun setChecked(isChecked: Boolean): DrawerItem<T> {
        this.isChecked = isChecked
        return this
    }
    open val isSelectable: Boolean
        get() = true
}
