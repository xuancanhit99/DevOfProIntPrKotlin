package com.xuancanhit.pr933

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xuancanhit.pr933.models.Person
import com.xuancanhit.pr933.ui.view.adapters.EmployeeAdapter
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerview:RecyclerView
    private lateinit var dataPerson: ArrayList<Person>
    private lateinit var btnSoftByName: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataPerson = ArrayList<Person>()
        dataPerson = deserializeObjectListTest()

        val sortedDataPersonAZ:List<Person> = dataPerson.sortedWith(compareBy { it.name })
        val sortedDataPersonZA = sortedDataPersonAZ.reversed()
        var check = true
        btnSoftByName = findViewById(R.id.btn_soft_by_name)
        btnSoftByName.setOnClickListener {
            val adapterEmployee:EmployeeAdapter
            if(check) {
                adapterEmployee = EmployeeAdapter(sortedDataPersonAZ)
                check = false
            }
            else {
                adapterEmployee = EmployeeAdapter(sortedDataPersonZA)
                check = true
            }
            recyclerview.adapter = adapterEmployee
        }

        addControls()
        readData()
    }

    private fun readData() {
        val adapterEmployee = EmployeeAdapter(dataPerson)
        recyclerview.adapter = adapterEmployee
    }

    private fun addControls() {
        recyclerview = findViewById(R.id.rv_items)
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun readFileAsTextUsingInputStream() =
        assets.open("data_persons.json").readBytes().toString(Charset.defaultCharset())
        //resources.openRawResource(R.raw.data_persons).readBytes().toString(Charset.defaultCharset())

    private fun deserializeObjectListTest(): ArrayList<Person> {
        val person = object : TypeToken<ArrayList<Person>>() {}.type
        return Gson().fromJson(
            readFileAsTextUsingInputStream(),
            person
        )
    }


}