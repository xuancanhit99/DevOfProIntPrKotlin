package com.xuancanhit.pr933.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

data class Person(val name: String, val gender: Int, val phone: String)

fun readFileAsTextUsingInputStream(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun deserializeObjectListTest() {
    val person = object : TypeToken<ArrayList<Person>>() {}.type
    val personList = Gson().fromJson<ArrayList<Person>>(readFileAsTextUsingInputStream("pr933/src/main/assets/data_persons.json"), person)
    for (i in 0 until personList.size) {
        println(personList[i].name)
    }

}

fun main() {
    val listPerson: ArrayList<Person> = ArrayList<Person>()
    val listGenderOption = listOf(-1, 0, 1)
    val listAlphabet = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    var randomGender: Int
    var randomAlphabet: String

    val path = "pr933/src/main/assets/data_persons.json"
    for (i in 1..20) {
        randomGender = listGenderOption.random()
        randomAlphabet = listAlphabet.random()
        listPerson.add(
            Person(
                randomAlphabet+ "Name $i", randomGender, "+78985894400$i"
            )
        )
    }

    try {
        PrintWriter(FileWriter(path)).use {
            val gson = Gson()
            val jsonList:String = gson.toJson(
                listPerson
            )
            it.write(jsonList)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    deserializeObjectListTest()
}