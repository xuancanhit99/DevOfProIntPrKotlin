package com.xuancanhit.sims.tool.nav.top

import android.view.View
import android.view.ViewGroup

class SpaceItem(private val spaceDp: Int) : DrawerItem<SpaceItem.ViewHolder>() {
    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val c = parent.context
        val view = View(c)
        val height = (c.resources.displayMetrics.density * spaceDp).toInt()
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            height
        )
        return ViewHolder(view)
    }

    override fun bindViewHolder(holder: ViewHolder) {}
    override val isSelectable: Boolean
        get() = false

    class ViewHolder(itemView: View?) : DrawerAdapter.ViewHolder(
        itemView!!
    )
}
