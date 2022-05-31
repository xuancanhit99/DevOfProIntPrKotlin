package com.xuancanhit.sims

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.xuancanhit.sims.ui.activities.student.StudentLoginActivity
import com.xuancanhit.sims.ui.activities.student.StudentRegisterActivity
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    //Time Splash before run app
    private val splashTime = 1500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StudentLoginActivity::class.java))
            finish()
        }, splashTime.toLong())
    }


    companion object {
        fun isEmailValid(editText: EditText): Boolean {
            val emailC = editText.text.toString()
            if (emailC == "") return true
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(emailC)
            return matcher.matches()
        }

        fun isEmptyEditText(editText: EditText): Boolean {
            val str = editText.text.toString()
            return TextUtils.isEmpty(str)
        }
    }
}