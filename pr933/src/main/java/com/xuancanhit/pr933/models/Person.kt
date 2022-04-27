package com.xuancanhit.pr933.models

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

data class Person(val name: String, val gender: Int, val phone: String) {
}



fun main() {
    val list = listOf(-1, 0, 1)
    val per = Person(
        "Name 1", list.random(), "+789858944001"
    )

    val path = "myData.json"
    try {
        val mapper = ObjectMapper()
        mapper.writeValue(File(path), per)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun main1() {
    val list = listOf(-1, 0, 1)
    var randomGender: Int
    val path = "../myData.json"
    try {
        for (i in 1..20) {
            randomGender = list.random()
            PrintWriter(FileWriter(path)).use {
                val gson = Gson()
                val jsonString = gson.toJson(
                    Person(
                        "Name $i", randomGender, "+78985894400$i"
                    )
                )
                it.write(jsonString)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}