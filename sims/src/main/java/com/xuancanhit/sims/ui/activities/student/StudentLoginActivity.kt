package com.xuancanhit.sims.ui.activities.student

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.activity_student_login.*
import kotlinx.android.synthetic.main.layout_student_login.*
import kotlinx.android.synthetic.main.layout_student_register.*
import java.util.regex.Pattern

class StudentLoginActivity : AppCompatActivity() {

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

    private fun initView() {

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
                studentLogin()
                rememberMe()
            }
            false
        })

        //Image View Close
        iv_stu_login_close.setOnClickListener {
            finish()
        }

        //To Login Admin
        tv_stu_login_to_login_admin.setOnClickListener {

        }

        //To Register
        tv_stu_login_to_register.setOnClickListener {
            startActivity(Intent(this, StudentRegisterActivity::class.java))
            finish()
        }

        //Button Login
        btn_stu_login.setOnClickListener {
            email = edt_stu_login_email.text.toString().trim()
            password = edt_stu_login_password.text.toString().trim()

            if (isEmptyEditText(edt_stu_login_email)) {
                edt_stu_login_email.error = "Enter email address!"
            }

            if (isEmptyEditText(edt_stu_login_password))
                edt_stu_login_password.error = "Enter password!"
            if (password.length < 6)
                edt_stu_login_password.error = "Password too short, enter minimum 6 characters!"

            if (MainActivity.isEmailValid(edt_stu_login_email)) {
                studentLogin()
                rememberMe()
            } else {
                edt_stu_login_email.error = "Email address not valid"
            }
        }

    }

    private fun isEmptyEditText(editText: EditText): Boolean {
        val str = editText.text.toString()
        return TextUtils.isEmpty(str)
    }

    private fun studentLogin() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()

                        //val user = auth.currentUser
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        //updateUI(null)
                    }
                }
        } else {
            Toast.makeText(this, "Something wrong. Please check again!", Toast.LENGTH_SHORT).show()
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