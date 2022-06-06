package com.xuancanhit.sims.ui.fragments.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.xuancanhit.sims.ui.activities.admin.AdminLoginActivity
import com.xuancanhit.sims.ui.activities.student.StudentLoginActivity
import com.xuancanhit.sims.ui.fragments.student.ChatFragment
import com.xuancanhit.sims.ui.fragments.student.LearningResultsFragment
import com.xuancanhit.sims.ui.fragments.student.ProfileFragment
import com.xuancanhit.sims.ui.fragments.student.StudentEditProfileFragment
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.main.fragment_admin_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class AdminHomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var admin: Admin? = null

    private var rootView:View? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin_home, container, false)

        // Initialize
        auth = Firebase.auth
        database = Firebase.database.reference

        rootView?.btn_ad_my_profile?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container_admin, AdminProfileFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_ad_update_profile?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container_admin, AdminEditProfileFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_ad_my_profile?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container_admin, AdminProfileFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_ad_list_student?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container_admin, AdminListStudentFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_ad_menu_chat?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container_admin, AdminChatFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_ad_add_student?.setOnClickListener {

            var url = "https://online-edu.mirea.ru/my/"
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://$url";
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
//            val fragmentTransaction = fragmentManager?.beginTransaction()
//            fragmentTransaction?.replace(R.id.body_container_admin, AdminAddStudentFragment())
//            fragmentTransaction?.commit()
        }

        rootView?.btn_ad_logout?.setOnClickListener {
            signOutAccount()
        }


        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_ad_home)
        return rootView
    }

    //Sign out
    private fun signOutAccount() {

        //Dialog Are you sure
        MaterialAlertDialogBuilder(
            requireContext())
            .setTitle(activity?.resources?.getString(R.string.title_dialog_logout))
            .setMessage(resources.getString(R.string.message_are_you_sure))
//            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
//                // Respond to neutral button press
//
//            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                // Respond to positive button press
                auth.signOut()
                startActivity(Intent(requireActivity(), AdminLoginActivity::class.java))
                requireActivity().finish()
            }
            .show()
    }


    private fun reload(name: String = "", img:String = "") {
        rootView?.tv_ad_menu_name?.text = name
        Picasso.get()
            .load(img)
            .placeholder(R.drawable.graduated)
            .error(R.drawable.graduated)
            .into(rootView?.iv_ad_menu_avt)
    }



    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload(currentUser.displayName.toString(), currentUser.photoUrl.toString(),"")
            database.child("Admins").child(currentUser.uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    admin = snapshot.getValue(Admin::class.java)
                    admin?.let {
                        reload(it.name.toString(), it.img.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }




}