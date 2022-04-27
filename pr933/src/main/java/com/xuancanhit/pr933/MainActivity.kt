package com.xuancanhit.pr933

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xuancanhit.pr933.models.Person
import com.xuancanhit.pr933.ui.view.adapters.EmployeeAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.rv_items)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val dataPerson = ArrayList<Person>()
        for (i in 1..20) {
            dataPerson.add(Person("Name $i", 1, "89858944005$i"))
        }
        for (i in 20..40) {
            dataPerson.add(Person("Name $i", 0, "89858944005$i"))
        }
        for (i in 40..50) {
            dataPerson.add(Person("Name $i", -1, "89858944005$i"))
        }

        val adapterEmployee = EmployeeAdapter(dataPerson)
        recyclerview.adapter = adapterEmployee
    }
}