package com.xuancanhit.pr8task31

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var edtMin: EditText
    lateinit var edtMax: EditText
    lateinit var btnStartGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI();
        initView();
    }

    private fun initView() {
        btnStartGame.setOnClickListener {
            if (edtMin.text.toString() == "" || edtMax.text.toString() == "") {
                Toast.makeText(this, "Input fields cannot be empty",
                    Toast.LENGTH_SHORT).show()
            } else if (edtMax.text.toString().toInt() <= edtMin.text.toString().toInt()) {
                Toast.makeText(this, "Invalid min, max value",
                    Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, GuessNumber::class.java)
                intent.putExtra("MAIN_MIN", edtMin.text.toString().toInt())
                intent.putExtra("MAIN_MAX", edtMax.text.toString().toInt())
                startActivity(intent)
                finish()
            }
        }
    }
    private fun initUI() {
        edtMin = findViewById(R.id.edt_main_min)
        edtMax = findViewById(R.id.edt_main_max)
        btnStartGame = findViewById(R.id.btn_main_start_game)
    }
}