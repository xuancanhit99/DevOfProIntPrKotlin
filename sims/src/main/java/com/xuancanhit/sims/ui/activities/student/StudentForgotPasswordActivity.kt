package com.xuancanhit.sims.ui.activities.student

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xuancanhit.sims.R
import kotlinx.android.synthetic.main.activity_student_forgot_password.*
import kotlinx.android.synthetic.main.layout_student_forgot_password.*

class StudentForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_forgot_password)

        auth = Firebase.auth
        initView()

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_stu_forgot_pass_close -> finish()
            R.id.tv_stu_forgot_pass_to_login -> loginActivity()
            R.id.btn_stu_forgot_pass_reset -> resetPassword()
        }
    }

    private fun loginActivity() {
        startActivity(Intent(this, StudentLoginActivity::class.java))
        finish()
    }


    private fun initView() {
        //ImageView Close
        iv_stu_forgot_pass_close.setOnClickListener(this)
        //To Login
        tv_stu_forgot_pass_to_login.setOnClickListener(this)
        //Reset Password
        btn_stu_forgot_pass_reset.setOnClickListener(this)
    }

    private fun resetPassword() {
        email = edt_stu_forgot_pass_email.text.toString().trim()

        if (email.isEmpty()) {
            edt_stu_forgot_pass_email.error = "Enter email address!"
            edt_stu_forgot_pass_email.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_stu_forgot_pass_email.error = "Email address not valid!"
            edt_stu_forgot_pass_email.requestFocus()
        }

        progressBarStudentForgotPassword.visibility = View.VISIBLE

        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Check your email to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                        edt_stu_forgot_pass_email.setText("")
                        progressBarStudentForgotPassword.visibility = View.GONE
                    } else {
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressBarStudentForgotPassword.visibility = View.GONE
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Fields cannot be left blank. Please check again!",
                Toast.LENGTH_SHORT
            ).show()
            progressBarStudentForgotPassword.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        loginActivity()
    }
}