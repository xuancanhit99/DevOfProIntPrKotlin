package com.xuancanhit.sims.ui.activities.admin

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.InternetDialog
import com.xuancanhit.sims.tool.ProgressButton
import com.xuancanhit.sims.ui.activities.student.StudentLoginActivity
import com.xuancanhit.sims.ui.activities.student.StudentMainActivity
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.android.synthetic.main.layout_admin_login.*
import kotlinx.android.synthetic.main.layout_student_login.*
import kotlinx.android.synthetic.main.progress_button_layout.*


class AdminLoginActivity : AppCompatActivity() {

    private lateinit var loginPrefsEditor: SharedPreferences.Editor

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var progressButtonStudentLogin: ProgressButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        MainActivity.hideStatusBar(window)

        auth = Firebase.auth

        //Init View

        //Remember Me
        val loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        loginPrefsEditor = loginPreferences.edit()
        val rememberMeCheck = loginPreferences.getBoolean("ADMIN_REMEMBER_ME", false)
        if (rememberMeCheck) {
            edt_ad_login_email.setText(loginPreferences.getString("ADMIN_EMAIL", ""))
            edt_ad_login_password.setText(loginPreferences.getString("ADMIN_PASSWORD", ""))
            cb_ad_login_remember_me.isChecked = true
        }

        //Login When Enter - Done
        edt_ad_login_password.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handlerButtonLogin()
                rememberMe()
            }
            false
        })

        //Image View Close
        iv_ad_login_close.setOnClickListener {
            finish()
        }

        tv_ad_login_to_login_stu.setOnClickListener {
            startActivity(Intent(this, StudentLoginActivity::class.java))
            finish()
        }

        textView_progress_button.text = "Login"
        btn_progress_button.setOnClickListener {
            handlerButtonLogin()
        }
    }

    private fun handlerButtonLogin() {
        progressButtonStudentLogin = ProgressButton(
            cardView_progress_button,
            constraint_layout_progress_button,
            progressBar_progress_button,
            textView_progress_button
        )
        progressButtonStudentLogin.buttonActivated(
            "Logging in...",
            AnimationUtils.loadAnimation(this, R.anim.fade_in)
        )
        Handler(Looper.getMainLooper()).postDelayed({
            loginAdmin()
        }, 1000.toLong())
    }

    private fun loginAdmin() {
        // CALL getInternetStatus() function to check for internet and display error dialog
        InternetDialog(this).internetStatus


        email = edt_ad_login_email.text.toString().trim()
        password = edt_ad_login_password.text.toString().trim()

        if (password.isEmpty()) {
            edt_ad_login_password.error = "Enter password!"
            edt_ad_login_password.requestFocus()
        }

        if (email.isEmpty()) {
            edt_ad_login_email.error = "Enter email address!"
            edt_ad_login_email.requestFocus()
        }

        if (password.length < 6)
            edt_ad_login_password.error = "Password too short, enter minimum 6 characters!"

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_ad_login_email.error = "Email address not valid"
            edt_ad_login_email.requestFocus()
        }



        loginAuthentication()
        rememberMe()
    }

    private fun rememberMe() {
        if (cb_ad_login_remember_me.isChecked) {
            loginPrefsEditor.putBoolean("ADMIN_REMEMBER_ME", true)
            loginPrefsEditor.putString("ADMIN_EMAIL", email)
            loginPrefsEditor.putString("ADMIN_PASSWORD", password)
        } else {
            loginPrefsEditor.clear()
        }
        loginPrefsEditor.apply()
    }

    private fun loginAuthentication() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        user?.let {

////-------------------Verify account
//                            if (user.isEmailVerified) {
//                                //Login thanh cong
////-------------------Verify account

                            progressButtonStudentLogin.buttonFinished(
                                "Logged in",
                                AnimationUtils.loadAnimation(this, R.anim.fade_in)
                            )
                            Handler(Looper.getMainLooper()).postDelayed({
                                //Reset Button
                                progressButtonStudentLogin.buttonReset(
                                    "Login",
                                    AnimationUtils.loadAnimation(this, R.anim.fade_in)
                                )
                            }, 1000.toLong())

                            Toast.makeText(
                                this,
                                "Welcome " + user.displayName + "!",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, AdminMainActivity::class.java))
                            finish()

////-------------------Verify account
//                            } else {
//                                //Login loi
//                                progressButtonStudentLogin.buttonError(
//                                    "Login in error",
//                                    AnimationUtils.loadAnimation(this, R.anim.fade_in)
//                                )
//                                Handler(Looper.getMainLooper()).postDelayed({
//                                    //Reset Button
//                                    progressButtonStudentLogin.buttonReset(
//                                        "Login",
//                                        AnimationUtils.loadAnimation(this, R.anim.fade_in)
//                                    )
//                                }, 1000.toLong())
//                                user.sendEmailVerification()
//                                EmailDialog(this).showVerifyEmailDialog(
//                                    "Verify Email",
//                                    "Check your email to verify your account"
//                                )
//                            }
////-------------------Verify account


                        }
                    } else {
                        //Login loi
                        progressButtonStudentLogin.buttonError(
                            "Login in error",
                            AnimationUtils.loadAnimation(this, R.anim.fade_in)
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            //Reset Button
                            progressButtonStudentLogin.buttonReset(
                                "Login",
                                AnimationUtils.loadAnimation(this, R.anim.fade_in)
                            )
                        }, 1000.toLong())
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        //progressButtonStudentLogin.buttonFinished()
                    }
                }
        } else {
            //Login loi
            progressButtonStudentLogin.buttonError(
                "Login in error",
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                //Reset Button
                progressButtonStudentLogin.buttonReset(
                    "Login",
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


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, AdminMainActivity::class.java))
            finish()
        }
    }

}