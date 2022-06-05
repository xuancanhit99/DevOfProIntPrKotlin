package com.xuancanhit.sims.ui.fragments.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.main.fragment_learning_results.view.*

class LearningResultsFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var student: Student? = null

    private var rootView:View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_learning_results, container, false)

        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_learning_results)

        // Initialize
        auth = Firebase.auth
        database = Firebase.database.reference

        return rootView
    }

    private fun reload(name: String, group: String, img: String, math: String, eng: String, phy: String, info: String, pro: String, eco: String, phi: String) {
        rootView?.tv_stu_result_name?.text = name;
        rootView?.tv_stu_result_group?.text = group;
        rootView?.tv_stu_result_math?.text = math;
        rootView?.tv_stu_result_eng?.text = eng;
        rootView?.tv_stu_result_phy?.text = phy;
        rootView?.tv_stu_result_info?.text = info;
        rootView?.tv_stu_result_pro?.text = pro;
        rootView?.tv_stu_result_eco?.text = eco;
        rootView?.tv_stu_result_phi?.text = phi;

        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(rootView?.iv_stu_result_avt)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("Students").child(currentUser.uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    student = snapshot.getValue(Student::class.java)
                    student?.let {
                        reload(
                            it.name.toString(),
                            it.group.toString(),
                            it.img.toString(),
                            it.results?.math.toString(),
                            it.results?.eng.toString(),
                            it.results?.phy.toString(),
                            it.results?.info.toString(),
                            it.results?.pro.toString(),
                            it.results?.eco.toString(),
                            it.results?.phi.toString()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

}