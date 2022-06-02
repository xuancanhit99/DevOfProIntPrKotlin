package com.xuancanhit.sims.tool.nav.top

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//class DrawerAdapter(items: List<DrawerItem>) :
//    RecyclerView.Adapter<DrawerAdapter.ViewHolder?>() {
//    private val items: List<DrawerItem>
//    private val viewTypes: MutableMap<Class<out DrawerItem?>, Int>
//    private val holderFactories: SparseArray<DrawerItem>
//    private var listener: OnItemSelectedListener? = null
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val holder: ViewHolder = holderFactories[viewType].createViewHolder(parent)
//        holder.adapter = this
//        return holder
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        items[position].bindViewHolder(holder)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return viewTypes[items[position].getClass()]!!
//    }
//
//    private fun processViewTypes() {
//        var type = 0
//        for (item in items) {
//            if (!viewTypes.containsKey(item.getClass())) {
//                viewTypes[item.getClass()] = type
//                holderFactories.put(type, item)
//                type++
//            }
//        }
//    }
//
//    fun setSelected(position: Int) {
//        val newChecked: DrawerItem = items[position]
//        if (!newChecked.isSelectable()) {
//            return
//        }
//        for (i in items.indices) {
//            val item: DrawerItem = items[i]
//            if (item.isChecked()) {
//                item.setChecked(false)
//                notifyItemChanged(i)
//                break
//            }
//        }
//        newChecked.setChecked(true)
//        notifyItemChanged(position)
//        if (listener != null) {
//            listener!!.onItemSelected(position)
//        }
//    }
//
//    fun setListener(listener: OnItemSelectedListener?) {
//        this.listener = listener
//    }
//
//    internal abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
//        View.OnClickListener {
//        private val adapter: DrawerAdapter? = null
//        override fun onClick(v: View) {
//            adapter!!.setSelected(adapterPosition)
//        }
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//    }
//
//    interface OnItemSelectedListener {
//        fun onItemSelected(position: Int)
//    }
//
//    init {
//        this.items = items
//        viewTypes = HashMap<Class<out DrawerItem?>, Int>()
//        holderFactories = SparseArray<DrawerItem>()
//        processViewTypes()
//    }
//}




class DrawerAdapter : RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private var items: List<DrawerItem<ViewHolder>>

    private var viewTypes: MutableMap<Class<out DrawerItem<ViewHolder>?>, Int>
    //private var viewTypes: Map<Class<DrawerItem<ViewHolder>>, Int>
    private var holderFactories: SparseArray<DrawerItem<ViewHolder>>

    private var listener: OnItemSelectedListener? = null



    constructor(items: List<DrawerItem<ViewHolder>>) {
        this.items = items
        this.viewTypes = HashMap()
        this.holderFactories = SparseArray()
        processViewTypes()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder: ViewHolder = holderFactories[viewType].createViewHolder(parent)
        holder.adapter = this
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].bindViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypes[items[position]::class.java]!!
    }


    //class.java
    private fun processViewTypes() {
        var type = 0
        for (item in items) {
            if (!viewTypes.containsKey(item::class.java)) {
                viewTypes[item::class.java] = type
                holderFactories.put(type, item)
                type++
            }
        }
    }

    fun setSelected(position: Int) {
        val newChecked: DrawerItem<ViewHolder> = items[position]
        if (!newChecked.isSelectable) {
            return
        }
        for (i in items.indices) {
            val item: DrawerItem<ViewHolder> = items[i]
            if (item.isChecked) {
                item.setChecked(false)
                notifyItemChanged(i)
                break
            }
        }
        newChecked.setChecked(true)
        notifyItemChanged(position)

        if (listener != null) {
            listener!!.onItemSelected(position)
        }
    }

    fun setListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        lateinit var adapter: DrawerAdapter



        override fun onClick(v: View) {
            adapter.setSelected(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

}
