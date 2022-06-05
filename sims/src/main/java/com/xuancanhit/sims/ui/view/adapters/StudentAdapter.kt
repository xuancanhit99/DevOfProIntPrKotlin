package com.xuancanhit.sims.ui.view.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.interfaces.ItemClickListener
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity


//-----------------Firebase recyclerview---------------------
class StudentAdapter(options: FirebaseRecyclerOptions<Student>) :
    FirebaseRecyclerAdapter<Student, StudentAdapter.StudentViewHolder>(
        options
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_for_recycler_view_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int, model: Student) {
        holder.tvName.text = model.name.toString()
        holder.tvClass.text = model.group.toString()

        Picasso.get()
            .load(model.img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(holder.ivImg)

        //Click for RecycleView

        //Click for RecycleView

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View?, position: Int, isLongClick: Boolean) {
                if (isLongClick) {
                    Toast.makeText(
                        view?.context,
                        "Student: " + model.name,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    //Pass data to admin update student fragment
                    Toast.makeText(
                        view?.context,
                        model.no,
                        Toast.LENGTH_SHORT
                    ).show()

                    //Pass data uid to AdminUpdateStudentFragment
                    model.no?.let {
                        (view?.context as PassDataFragmentAndActivity?)!!.passDataComm(
                            it
                        )
                    }

                }
            }
        })

    }



    class StudentViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView), View.OnClickListener, View.OnLongClickListener {
        val ivImg: ImageView = itemView.findViewById(R.id.iv_stu_item_rv)
        val tvName: TextView = itemView.findViewById(R.id.tv_stu_item_rv_name)
        val tvClass: TextView = itemView.findViewById(R.id.tv_stu_item_rv_group)
        private var itemClickListener: ItemClickListener? = null
        override fun onClick(v: View?) {
            itemClickListener!!.onClick(v, adapterPosition, false)
        }

        override fun onLongClick(v: View?): Boolean {
            itemClickListener!!.onClick(v, adapterPosition, true)
            return true
            //return false; // if not use
        }

        @JvmName("setItemClickListener1")
        fun setItemClickListener(itemClickListener: ItemClickListener?) {
            this.itemClickListener = itemClickListener
        }
        init {
            //Turn On Click for RecycleView
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

    }

}

//-----------------Firebase recyclerview---------------------




////-----------------Recyclerview-------------------
//class StudentAdapter(private val mList: List<Student>) :
//    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): StudentViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_item_for_recycler_view_student, parent, false)
//        return StudentViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
//        val student = mList[position]
//
//        holder.tvName.text = student.name
//        holder.tvClass.text = student.group
//
//        Picasso.get()
//            .load(student.img)
//            .placeholder(R.drawable.graduated)
//            .error(R.drawable.graduated)
//            .into(holder.ivImg)
//    }
//
//    override fun getItemCount(): Int {
//        return mList.size
//    }
//
//
//    class StudentViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val ivImg: ImageView = itemView.findViewById(R.id.iv_stu_item_rv)
//        val tvName: TextView = itemView.findViewById(R.id.tv_stu_item_rv_name)
//        val tvClass: TextView = itemView.findViewById(R.id.tv_stu_item_rv_group)
//    }
//}
//
////-----------------Recyclerview-------------------


