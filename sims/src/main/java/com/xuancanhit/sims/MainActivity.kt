package com.xuancanhit.sims

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.*
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TypefaceSpan
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener
import com.xuancanhit.sims.ui.activities.student.StudentLoginActivity
import com.xuancanhit.sims.ui.activities.student.StudentRegisterActivity
import com.xuancanhit.sims.ui.fragments.student.StudentEditProfileFragment
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    //Time Splash before run app
    private val splashTime = 1500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StudentLoginActivity::class.java))
            finish()
        }, splashTime.toLong())
    }


    companion object {

        //Hide Status Bar
        fun hideStatusBar(window: Window) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }


        //Custom Font
        fun textWithMyFont(context: Context, text: String, fontId: Int, textStyle: Int): SpannableString {
            val myTypeface = Typeface.create(ResourcesCompat.getFont(context, fontId), textStyle)
            val string = SpannableString(text)
            string.setSpan(
                TypefaceSpan(myTypeface),
                0,
                string.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return string
        }

        //AestheticDialog
        fun aestheticDialog(activity: Activity, dialogStyle: DialogStyle, dialogType: DialogType, title: String, message: String) {
            AestheticDialog.Builder(activity, dialogStyle, dialogType)
                .setTitle(title)
                .setMessage(message)
                .setOnClickListener(object : OnDialogClickListener {
                    override fun onClick(dialog: AestheticDialog.Builder) {
                        dialog.dismiss()
                    }
                })
                .show()
        }

        //Toast
        fun toast(activity: Activity, text: String) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }

    }
}