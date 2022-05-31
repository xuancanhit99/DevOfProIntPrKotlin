package com.xuancanhit.sims.ui.activities.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import kotlinx.android.synthetic.main.activity_student_forgot_password.*
import kotlinx.android.synthetic.main.layout_student_forgot_password.*
import kotlinx.android.synthetic.main.layout_student_register.*

class StudentForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_forgot_password)


        iv_stu_forgot_pass_close.setOnClickListener {
            finish()
        }

        tv_stu_forgot_pass_to_login.setOnClickListener {
            startActivity(Intent(this, StudentLoginActivity::class.java))
            finish()
        }

        btn_stu_forgot_pass_reset.setOnClickListener {

            email = edt_stu_forgot_pass_email.text.toString().trim()

            if (email.isEmpty())
                edt_stu_forgot_pass_email.error = "Enter email address!"

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                resetPassword()

            } else {
                edt_stu_forgot_pass_email.error = "Email address not valid!"
            }
        }
    }

    private fun resetPassword() {
        if (email.isNotEmpty() ) {


        }
    }
}