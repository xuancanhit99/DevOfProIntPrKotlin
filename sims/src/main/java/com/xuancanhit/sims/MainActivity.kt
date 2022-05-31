package com.xuancanhit.sims

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
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
    }
}