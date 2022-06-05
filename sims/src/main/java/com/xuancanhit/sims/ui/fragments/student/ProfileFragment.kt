package com.xuancanhit.sims.ui.fragments.student

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xuancanhit.sims.R
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var student: Student? = null

    private lateinit var tvName: TextView
    private lateinit var tvNo: TextView
    private lateinit var tvDob: TextView
    private lateinit var tvGroup: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivImg: ImageView

    private lateinit var btnEdit: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize
        auth = Firebase.auth
        database = Firebase.database.reference

        tvName = rootView.findViewById(R.id.tv_stu_profile_name)
        tvNo = rootView.findViewById(R.id.tv_stu_profile_no)
        tvDob = rootView.findViewById(R.id.tv_stu_profile_dob)
        tvGroup = rootView.findViewById(R.id.tv_stu_profile_group)
        tvGender = rootView.findViewById(R.id.tv_stu_profile_gender)
        tvPhone = rootView.findViewById(R.id.tv_stu_profile_phone)
        tvEmail = rootView.findViewById(R.id.tv_stu_profile_email)
        ivImg = rootView.findViewById(R.id.iv_stu_profile_avt)
        //btnBack = rootView.findViewById(R.id.btn_stu_profile_back)
        btnEdit = rootView.findViewById(R.id.btn_stu_profile_update)

        btnEdit.setOnClickListener{
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.body_container, StudentEditProfileFragment())
                fragmentTransaction?.commit()
        }


        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_profile)
        return rootView
    }




    private fun reload(
        name: String = "",
        img: String = "",
        no: String = "",
        dob: String = "",
        group: String = "",
        gender: String = "",
        phone: String = "",
        email: String = ""
    ) {
        tvName.text = name
        tvNo.text = no
        tvDob.text = dob
        tvGroup.text = group
        if (gender == "0")
            tvGender.text = activity?.resources?.getString(R.string.female)
        else
            tvGender.text = activity?.resources?.getString(R.string.male)
        tvPhone.text = phone
        tvEmail.text = email

        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(ivImg)
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
                            it.img.toString(),
                            it.no.toString(),
                            it.dob.toString(),
                            it.group.toString(),
                            it.gender.toString(),
                            it.phone.toString(),
                            it.email.toString()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tv, btn
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }
}