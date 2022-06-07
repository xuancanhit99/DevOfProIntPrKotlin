package com.xuancanhit.sims.ui.activities.student

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.dialog.EmailDialog
import com.xuancanhit.sims.tool.dialog.InternetDialog
import com.xuancanhit.sims.tool.but.ProgressButton
import kotlinx.android.synthetic.main.activity_student_forgot_password.*
import kotlinx.android.synthetic.main.layout_student_forgot_password.*
import kotlinx.android.synthetic.main.progress_button_layout.*

class StudentForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String

    private lateinit var progressButtonStudentResetPassword: ProgressButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_forgot_password)

        MainActivity.hideStatusBar(window)

        auth = Firebase.auth
        initView()

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_stu_forgot_pass_close -> finish()
            R.id.tv_stu_forgot_pass_to_login -> loginActivity()
            R.id.btn_progress_button -> handlerButtonResetPassword()
        }
    }

    private fun handlerButtonResetPassword() {
        progressButtonStudentResetPassword = ProgressButton(
            cardView_progress_button,
            constraint_layout_progress_button,
            progressBar_progress_button,
            textView_progress_button
        )
        progressButtonStudentResetPassword.buttonActivated(
            "Resetting...",
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )
        Handler(Looper.getMainLooper()).postDelayed({
            resetPassword()
        }, 1000.toLong())
    }

    private fun loginActivity() {
        startActivity(Intent(this, StudentLoginActivity::class.java))
        finish()
    }


    @SuppressLint("SetTextI18n")
    private fun initView() {
        //ImageView Close
        iv_stu_forgot_pass_close.setOnClickListener(this)
        //To Login
        tv_stu_forgot_pass_to_login.setOnClickListener(this)
        //Reset Password
        textView_progress_button.text = "Reset Password"
        btn_progress_button.setOnClickListener(this)
    }

    private fun resetPassword() {
        // CALL getInternetStatus() function to check for internet and display error dialog
        InternetDialog(this).internetStatus

        email = edt_stu_forgot_pass_email.text.toString().trim()

        if (email.isEmpty()) {
            edt_stu_forgot_pass_email.error = "Enter email address!"
            edt_stu_forgot_pass_email.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_stu_forgot_pass_email.error = "Email address not valid!"
            edt_stu_forgot_pass_email.requestFocus()
        }



        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Login thanh cong
                        progressButtonStudentResetPassword.buttonFinished(
                            "Ready To Reset",
                            AnimationUtils.loadAnimation(this, R.anim.fade_in)
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            //Reset Button
                            progressButtonStudentResetPassword.buttonReset(
                                "Reset Password",
                                AnimationUtils.loadAnimation(this, R.anim.fade_in)
                            )
                        }, 1000.toLong())


                        EmailDialog(this).showVerifyEmailDialog("Check Your Email","We have sent a password reset link to your email. Please check")
                        edt_stu_forgot_pass_email.setText("")
                    } else {
                        //Reset Password loi
                        progressButtonStudentResetPassword.buttonError(
                            "Reset Error",
                            AnimationUtils.loadAnimation(this, R.anim.fade_in)
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            //Reset Button
                            progressButtonStudentResetPassword.buttonReset(
                                "Reset Password",
                                AnimationUtils.loadAnimation(this, R.anim.fade_in)
                            )
                        }, 1000.toLong())
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        } else {
            //Reset Password loi
            progressButtonStudentResetPassword.buttonError(
                "Reset Password Error",
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                //Reset Button
                progressButtonStudentResetPassword.buttonReset(
                    "Reset Password",
                    AnimationUtils.loadAnimation(this, R.anim.fade_in)
                )
            }, 1000.toLong())
            Toast.makeText(
                this,
                "Fields cannot be left blank. Please check again!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBackPressed() {
        loginActivity()
    }
}