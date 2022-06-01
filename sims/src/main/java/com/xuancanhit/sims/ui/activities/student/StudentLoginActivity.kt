package com.xuancanhit.sims.ui.activities.student

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.xuancanhit.sims.MainActivity
import com.xuancanhit.sims.R
import com.xuancanhit.sims.tool.InternetDialog
import kotlinx.android.synthetic.main.activity_student_login.*
import kotlinx.android.synthetic.main.layout_student_login.*
import kotlinx.android.synthetic.main.layout_student_register.*
import java.util.regex.Pattern

class StudentLoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginPrefsEditor: SharedPreferences.Editor

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)

        auth = Firebase.auth


        initView()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_stu_login_close -> finish()
//            R.id.tv_stu_login_to_login_admin
            R.id.tv_stu_login_to_register -> registerActivity()
            R.id.tv_stu_login_forgot_password ->forgotPasswordActivity()
            R.id.btn_stu_login -> loginStudent()
        }
    }

    private fun initView() {
        //Image View Close
        iv_stu_login_close.setOnClickListener(this)
        //To Login Admin
        tv_stu_login_to_login_admin.setOnClickListener(this)
        //To Register
        tv_stu_login_to_register.setOnClickListener(this)
        //Button Login
        btn_stu_login.setOnClickListener(this)
        //Forgot Password
        tv_stu_login_forgot_password.setOnClickListener(this)


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
                loginAuthentication()
                rememberMe()
            }
            false
        })
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

        progressBarStudentLogin.visibility = View.VISIBLE

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
                            if(user.isEmailVerified) {
                                Toast.makeText(this, "Welcome " + user.displayName + "!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, StudentMainActivity::class.java))
                                finish()
                            } else {
                                user.sendEmailVerification()
                                Toast.makeText(this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show()
                            }
                            progressBarStudentLogin.visibility = View.GONE

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
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressBarStudentLogin.visibility = View.GONE
                    }
                }
        } else {
            Toast.makeText(this, "Fields cannot be left blank. Please check again!", Toast.LENGTH_SHORT).show()
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