package com.xuancanhit.pr8task31

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class GuessNumber : AppCompatActivity() {

    private lateinit var btnYes: Button
    private lateinit var btnNo: Button
    private lateinit var tvText: TextView

    private var min = 0
    private var max = 0
    private var sMin = ""
    private var sMax = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_number)
        initUI()
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        min = intent.getIntExtra("MAIN_MIN", 0)
        max = intent.getIntExtra("MAIN_MAX", 0)
        sMin = min.toString();
        sMax = max.toString();
        Log.d("Value Min", min.toString())
        Log.d("Value Max", max.toString())
        tvText.text =
            "Guess the number from $sMin to $sMax\n" +
                    "Is your number bigger than " + (max + min) / 2 + "?"
        btnYes.setOnClickListener {
            middleRangeCalculate("Yes")
        }
        btnNo.setOnClickListener {
            middleRangeCalculate("No")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkStopAndGiveResult() {
        if (max - min == 1) {
            tvText.text =
                "Guess the number from $sMin to $sMax\nYour number is $max!"
            showDialog("Your number is $max!")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun middleRangeCalculate(status: String) {
        if (status == "Yes") {
            min = (max + min) / 2
            tvText.text =
                "Guess the number from $sMin to $sMax\n" +
                        "Is your number bigger than " + (max + min) / 2 + "?"
            Log.d("Value Min", min.toString())
            Log.d("Value Max", max.toString())
            checkStopAndGiveResult()
        } else if (status == "No") {
            max = (max + min) / 2
            if (max == min) {
                tvText.text =
                    "Guess the number from $sMin to $sMax\nYour number is $max!"
                showDialog("Your number is $max!")
            }
            tvText.text =
                "Guess the number from $sMin to $sMax\n" +
                        "Is your number bigger than " + (max + min) / 2 + "?"
            Log.d("Value Min", min.toString())
            Log.d("Value Max", max.toString())
            checkStopAndGiveResult()
        }
    }

//    private fun showDialog(title: String) {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.activity_guess_number)
//        val body = dialog.findViewById(R.id.tv_guess_number_text) as TextView
//        body.text = title
//        val yesBtn = dialog.findViewById(R.id.btn_guess_number_yes) as Button
//        val noBtn = dialog.findViewById(R.id.btn_guess_number_no) as TextView
//        yesBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//        noBtn.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }

    private fun showDialog(title: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage("Do you want to continue playing?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

//        builder.setNegativeButton("No") { dialog, which ->
//
//        }

        builder.setNeutralButton("No") { dialog, which ->
            finish()
        }
        builder.show()
    }

    private fun initUI() {
        btnNo = findViewById(R.id.btn_guess_number_no)
        btnYes = findViewById(R.id.btn_guess_number_yes)
        tvText = findViewById(R.id.tv_guess_number_text)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}