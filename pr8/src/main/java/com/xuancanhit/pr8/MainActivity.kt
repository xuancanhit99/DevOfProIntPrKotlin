package com.xuancanhit.pr8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.Array

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainViewModel
    private lateinit var tvCation: TextView
    private lateinit var tvSideALabel: TextView
    private lateinit var edtSideA: EditText
    private lateinit var tvSideBLabel: TextView
    private lateinit var edtSideB: EditText
    private lateinit var tvSideCLabel: TextView
    private lateinit var edtSideC: EditText
    private lateinit var spinner: Spinner
    private lateinit var btnGetSolution: Button
    private lateinit var tvSolution: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        val provider = ViewModelProvider(this)
        viewModel = provider.get(MainViewModel::class.java)
        observeViewModel()

        initView()
    }

    private fun initView() {
        var select: Int = 0
        var result: Int = 0
        var a: Int = 0
        var b: Int = 0
        var c: Int = 0
        val calculate = resources.getStringArray(R.array.calculate)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, calculate)
            spinner.adapter = adapter
            spinner.setSelection(0)
        }
        btnGetSolution.setOnClickListener {
            if (edtSideA.text.toString().toIntOrNull() != null &&
                edtSideB.text.toString().toIntOrNull() != null &&
                edtSideC.text.toString().toIntOrNull() != null
            ) {
                a = edtSideA.text.toString().toInt()
                b = edtSideB.text.toString().toInt()
                c = edtSideC.text.toString().toInt()
                if (a >= 0 && b >= 0 && c >= 0) {
                    viewModel.doCalculate(select, a, b, c)
                } else
                    tvSolution.text = "a, b, c должно быть >= 0"
            } else
                tvSolution.text = "Ошибка ввода"
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0)
                    select = 0
                else if (p2 == 1)
                    select = 1
                else if (p2 == 2)
                    select = 2
                //Toast.makeText(this@MainActivity, getString(R.string.selected_item) + " " + "" + calculate[p2], Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
    private fun observeViewModel() {
        viewModel.res.observe(this, Observer {
            tvSolution.text = it.toString()
        })
    }
    private fun initUI() {
        tvCation = findViewById(R.id.main_tv_caption)
        tvSideALabel = findViewById(R.id.main_tv_sideALabel)
        edtSideA = findViewById(R.id.main_edt_sideA)
        tvSideBLabel = findViewById(R.id.main_tv_sideBLabel)
        edtSideB = findViewById(R.id.main_edt_sideB)
        tvSideCLabel = findViewById(R.id.main_tv_sideCLabel)
        edtSideC = findViewById(R.id.main_edt_sideC)
        spinner = findViewById(R.id.main_spn)
        btnGetSolution = findViewById(R.id.main_btn_getSolution)
        tvSolution = findViewById(R.id.main_tv_solution)
    }
}