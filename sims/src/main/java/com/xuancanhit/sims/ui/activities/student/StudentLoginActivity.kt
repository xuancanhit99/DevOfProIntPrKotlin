package com.xuancanhit.sims.ui.activities.student

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.InternetDialog
import com.xuancanhit.sims.tool.ProgressButton
import com.xuancanhit.sims.tool.EmailDialog
import kotlinx.android.synthetic.main.activity_student_login.*
import kotlinx.android.synthetic.main.dialog_email.*
import kotlinx.android.synthetic.main.layout_student_login.*
import kotlinx.android.synthetic.main.progress_button_layout.*


class StudentLoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginPrefsEditor: SharedPreferences.Editor

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var progressButtonStudentLogin: ProgressButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        auth = Firebase.auth

        initView()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_stu_login_close -> finish()
//            R.id.tv_stu_login_to_login_admin
            R.id.tv_stu_login_to_register -> registerActivity()
            R.id.tv_stu_login_forgot_password -> forgotPasswordActivity()
            R.id.btn_progress_button -> handlerButtonLogin()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        //Image View Close
        iv_stu_login_close.setOnClickListener(this)
        //To Login Admin
        tv_stu_login_to_login_admin.setOnClickListener(this)
        //To Register
        tv_stu_login_to_register.setOnClickListener(this)
        //Forgot Password
        tv_stu_login_forgot_password.setOnClickListener(this)
        //View Button Login
        textView_progress_button.text = "Login"
        btn_progress_button.setOnClickListener(this)

        //Remember Me
        val loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        loginPrefsEditor = loginPreferences.edit()
        val rememberMeCheck = loginPreferences.getBoolean("STUDENT_REMEMBER_ME", false)
        if (rememberMeCheck) {
            edt_stu_login_email.setText(loginPreferences.getString("STUDENT_EMAIL", ""))
            edt_stu_login_password.setText(loginPreferences.getString("STUDENT_PASSWORD", ""))
            cb_stu_login_remember_me.isChecked = true
        }

        //Login When Enter - Done
        edt_stu_login_password.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handlerButtonLogin()
                rememberMe()
            }
            false
        })
    }


    //Hieu ung Button Login
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
            loginStudent()
        }, 1000.toLong())
    }

    private fun loginStudent() {
        // CALL getInternetStatus() function to check for internet and display error dialog
        InternetDialog(this).internetStatus


        email = edt_stu_login_email.text.toString().trim()
        password = edt_stu_login_password.text.toString().trim()

        if (password.isEmpty()) {
            edt_stu_login_password.error = "Enter password!"
            edt_stu_login_password.requestFocus()
        }

        if (email.isEmpty()) {
            edt_stu_login_email.error = "Enter email address!"
            edt_stu_login_email.requestFocus()
        }

        if (password.length < 6)
            edt_stu_login_password.error = "Password too short, enter minimum 6 characters!"

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_stu_login_email.error = "Email address not valid"
            edt_stu_login_email.requestFocus()
        }



        loginAuthentication()
        rememberMe()
    }

    private fun forgotPasswordActivity() {
        startActivity(Intent(this, StudentForgotPasswordActivity::class.java))
        finish()
    }

    private fun registerActivity() {
        startActivity(Intent(this, StudentRegisterActivity::class.java))
        finish()
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

                            //Verify account
                            if (user.isEmailVerified) {
                                //Login thanh cong
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
                                startActivity(Intent(this, StudentMainActivity::class.java))
                                finish()
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
                                user.sendEmailVerification()
                                EmailDialog(this).showVerifyEmailDialog("Verify Email", "Check your email to verify your account")
                            }
                            //progressButtonStudentLogin.buttonFinished()

//                            // Name, email address, and profile photo Url
//                            val name = user.displayName
//                            Log.d("LOG", name.toString())
//                            val email = user.email
//                            Log.d("LOG", email.toString())
//                            val photoUrl = user.photoUrl
//                            Log.d("LOG", photoUrl.toString())
//                            // Check if user's email is verified
//                            val emailVerified = user.isEmailVerified
//                            Log.d("LOG", emailVerified.toString())
//
//                            // The user's ID, unique to the Firebase project. Do NOT use this value to
//                            // authenticate with your backend server, if you have one. Use
//                            // FirebaseUser.getToken() instead.
//                            val uid = user.uid
//                            Log.d("LOG", uid.toString())
                        }
                        //updateUI(user)
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


    private fun rememberMe() {
        if (cb_stu_login_remember_me.isChecked) {
            loginPrefsEditor.putBoolean("STUDENT_REMEMBER_ME", true)
            loginPrefsEditor.putString("STUDENT_EMAIL", email)
            loginPrefsEditor.putString("STUDENT_PASSWORD", password)
        } else {
            loginPrefsEditor.clear()
        }
        loginPrefsEditor.apply()
    }
}