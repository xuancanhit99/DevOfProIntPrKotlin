package com.xuancanhit.sims.ui.activities.student

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import kotlinx.android.synthetic.main.activity_student_register.*
import kotlinx.android.synthetic.main.layout_student_register.*

class StudentRegisterActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_register)


        //Firebase services
        auth = Firebase.auth


        //Setup View
        initView()


    }

    private fun initView() {

        //Image View Close
        iv_stu_register_close.setOnClickListener {
            finish()
        }

        //Button Choose Photo
        btn_stu_register_choose_photo.setOnClickListener {


        }

        btn_stu_register.setOnClickListener {
            email = edt_stu_register_email.text.toString().trim()
            password = edt_stu_register_password.text.toString().trim()

            if (MainActivity.isEmptyEditText(edt_stu_register_name))
                edt_stu_register_email.error = "Enter your name!"
            if (MainActivity.isEmptyEditText(edt_stu_register_email))
                edt_stu_register_email.error = "Enter email address!"
            if (MainActivity.isEmptyEditText(edt_stu_register_password))
                edt_stu_register_password.error = "Enter password!"
            if (password.length < 6)
                edt_stu_register_password.error = "Password too short, enter minimum 6 characters!"


            if (MainActivity.isEmailValid(edt_stu_register_email)) {

                //Upload Info with photo

                //Upload Info
                uploadInfo()

            } else {
                edt_stu_register_email.error = "Email address not valid!"
            }
        }

    }

    private fun uploadInfo() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //val user = auth.currentUser.
                        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
//                    updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        //updateUI(null)
                    }
                }
        } else {
            Toast.makeText(this, "Something wrong. Please check again!", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        progressBar.visibility = View.GONE
//    }

    override fun onBackPressed() {
        finish()
    }
}