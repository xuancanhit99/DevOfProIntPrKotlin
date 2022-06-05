package com.xuancanhit.sims.ui.fragments.student

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
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
import com.xuancanhit.sims.model.Student
import com.xuancanhit.sims.ui.activities.student.StudentLoginActivity
import com.xuancanhit.sims.ui.interfaces.PassDataFragmentAndActivity
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var tvName: TextView
    private lateinit var tvGroup: TextView
    private lateinit var ivImg: ImageView

    private var student:Student? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize
        auth = Firebase.auth
        database = Firebase.database.reference


        tvName = rootView.findViewById(R.id.tv_stu_menu_name)
        tvGroup = rootView.findViewById(R.id.tv_stu_menu_group)
        ivImg = rootView.findViewById(R.id.iv_stu_menu_avt)

        rootView?.btn_stu_my_profile?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container, ProfileFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_stu_update_profile?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container, StudentEditProfileFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_stu_my_profile?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container, ProfileFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_stu_learning_results?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container, LearningResultsFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_stu_menu_chat?.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.body_container, ChatFragment())
            fragmentTransaction?.commit()
        }

        rootView?.btn_stu_time_table?.setOnClickListener {
            var url = "https://www.mirea.ru/schedule/"
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://$url";
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            //finish()
        }

        rootView?.btn_stu_logout?.setOnClickListener {
            signOutAccount()
        }


        (activity as PassDataFragmentAndActivity?)!!.setNavState(R.id.nav_home)
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
                startActivity(Intent(requireActivity(), StudentLoginActivity::class.java))
                requireActivity().finish()
            }
            .show()
    }


    private fun reload(name: String = "", img:String = "", group: String="") {
        tvName.text = name
        tvGroup.text = group
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
            //reload(currentUser.displayName.toString(), currentUser.photoUrl.toString(),"")
            database.child("Students").child(currentUser.uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    student = snapshot.getValue(Student::class.java)
                    student?.let {
                        reload(it.name.toString(), it.img.toString(), it.group.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {

    }
}