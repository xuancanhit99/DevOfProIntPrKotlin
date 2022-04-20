package com.xuancanhit.pr7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var toggleButton: ToggleButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var button: Button
    private lateinit var textView: TextView

    private var radio = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initView()
    }

    private fun initView() {
        editText.setText("")
        textView.text = ""
        var solution = ""

        button.setOnClickListener {
            solution = solution.plus("EditText: " + editText.text.toString())

            if (checkBox.isChecked)
                solution = solution.plus("\nCheckBox: ON")
            else
                solution = solution.plus("\nCheckBox: OFF")

            if (toggleButton.isChecked)
                solution = solution.plus("\nToggleButton: ON")
            else
                solution = solution.plus("\nToggleButton: OFF")

            if (radioButton1.isChecked)
                solution = solution.plus("\nRadioGroup: RadioButton 1")
            if (radioButton2.isChecked)
                solution = solution.plus("\nRadioGroup: RadioButton 2")

            textView.text = solution
            solution = ""
        }


    }

    private fun initUI() {
        editText = findViewById(R.id.main_edt)
        checkBox = findViewById(R.id.main_cb)
        toggleButton = findViewById(R.id.main_tgg_btn)
        radioGroup = findViewById(R.id.main_rdg)
        radioButton1 = findViewById(R.id.main_rd_btn1)
        radioButton2 = findViewById(R.id.main_rd_btn2)
        button = findViewById(R.id.main_btn)
        textView = findViewById(R.id.main_tv)
    }
}