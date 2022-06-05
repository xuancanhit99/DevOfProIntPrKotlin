package com.xuancanhit.sims.ui.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.xuancanhit.sims.model.Admin
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.fragments.student.StudentEditProfileFragment
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_admin_profile.view.*


class AdminProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var admin: Admin? = null


    private lateinit var tvNo: TextView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var ivAdAva: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_admin_profile, container, false)

        // Initialize
        auth = Firebase.auth
        database = Firebase.database.reference


        tvEmail = rootView.findViewById(R.id.tv_ad_profile_email)
        tvName = rootView.findViewById(R.id.tv_ad_profile_name)
        tvNo = rootView.findViewById(R.id.tv_ad_profile_no)
        tvPhone = rootView.findViewById(R.id.tv_ad_profile_phone)
        ivAdAva = rootView.findViewById(R.id.iv_ad_profile_avt)




        rootView.btn_ad_profile_update.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container_admin, AdminEditProfileFragment())
            fragmentTransaction?.commit()
        }
        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_ad_profile)
        return rootView
    }


    private fun reload(
        name: String = "",
        img: String = "",
        no: String = "",
        phone: String = "",
        email: String = ""
    ) {
        tvName.text = name
        tvNo.text = no
        tvPhone.text = phone
        tvEmail.text = email
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(ivAdAva)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("Admins").child(currentUser.uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    admin = snapshot.getValue(Admin::class.java)
                    admin?.let {
                        reload(
                            it.name.toString(),
                            it.img.toString(),
                            it.no.toString(),
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


    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }
}