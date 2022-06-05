package com.xuancanhit.sims.ui.fragments.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import com.xuancanhit.sims.ui.view.adapters.StudentAdapter
import kotlinx.android.synthetic.main.fragment_admin_list_student.view.*


class AdminListStudentFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: StudentAdapter


//    private lateinit var listStudent: ArrayList<Student>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_admin_list_student, container, false)

        //Init firebase
        database = Firebase.database.reference


//        //-----------------Recycler View---------------------
//        val myDatabaseStudent = database.child("Students")
//        rootView.rv_items_student.setHasFixedSize(true)
//        rootView.rv_items_student.layoutManager = LinearLayoutManager(requireContext())
//        listStudent = ArrayList()
//        adapter = StudentAdapter(listStudent)
//        rootView.rv_items_student.adapter = adapter
//        database.child("Students").addValueEventListener(object : ValueEventListener{
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (dataSnapshot in snapshot.children) {
//                    val student: Student? = dataSnapshot.getValue(Student::class.java)
//                    if (student != null) {
//                        listStudent.add(student)
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//        //-----------------Recycler View---------------------


        //-----------------Firebase recyclerview-------------------
        rootView.rv_items_student.layoutManager = LinearLayoutManager(requireContext())
        val options: FirebaseRecyclerOptions<Student> = FirebaseRecyclerOptions.Builder<Student>()
            .setQuery(database.child("Students").orderByChild("name"), Student::class.java)
            .build()
        adapter = StudentAdapter(options)
        rootView.rv_items_student.adapter = adapter



        //-----------------Firebase recyclerview-------------------

        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_ad_list_student)
        return rootView
    }

    //-----------------Firebase recyclerview-------------------
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
    //-----------------Firebase recyclerview-------------------

}