package com.xuancanhit.pr933.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xuancanhit.pr933.R
import com.xuancanhit.pr933.models.Person

class EmployeeAdapter(private val mList: List<Person>) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val person = mList[position]

        when (person.gender) {
            1 -> holder.ivImg.setImageResource(R.drawable.male)
            0 -> holder.ivImg.setImageResource(R.drawable.female)
            else -> holder.ivImg.setImageResource(R.drawable.unknown)
        }

        holder.tvName.text = person.name
        holder.tvPhone.text = person.phone
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class EmployeeViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivImg: ImageView = itemView.findViewById(R.id.iv_img)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
    }
}