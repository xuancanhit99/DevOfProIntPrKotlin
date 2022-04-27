package com.xuancanhit.pr832

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var edtText: EditText
    private lateinit var btnRunProcess: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonMakeACall: RadioButton
    private lateinit var radioButtonOpenWebsite: RadioButton
    private lateinit var radioButtonOpenMap: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI();
        initView();
    }

    private fun initView() {
        radioButtonOpenWebsite.isChecked = true

        radioButtonOpenMap.setOnClickListener {
            edtText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_NUMBER
            edtText.setText("")
        }
        radioButtonOpenWebsite.setOnClickListener {
            edtText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
            edtText.setText("")
        }
        radioButtonMakeACall.setOnClickListener {
            edtText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_NUMBER
            edtText.setText("")
        }


        btnRunProcess.setOnClickListener {
            if (radioButtonOpenWebsite.isChecked) {
                var url = edtText.text.toString()
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://$url";
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
                //finish()
            }

            if (radioButtonOpenMap.isChecked) {
                var geo = edtText.text.toString()
                val gmmIntentUri = Uri.parse("geo:$geo")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                mapIntent.resolveActivity(packageManager)?.let {
                    startActivity(mapIntent)
                }

            }

            if (radioButtonMakeACall.isChecked) {
                val phone = edtText.text.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }


        }
    }

    private fun initUI() {
        edtText = findViewById(R.id.edt_main_text)
        btnRunProcess = findViewById(R.id.btn_main_run_process)
        radioGroup = findViewById(R.id.rdg_main)
        radioButtonMakeACall = findViewById(R.id.rd_btn_main_number_phone)
        radioButtonOpenMap = findViewById(R.id.rd_btn_main_geo_point)
        radioButtonOpenWebsite = findViewById(R.id.rd_btn_main_web_address)
    }
}