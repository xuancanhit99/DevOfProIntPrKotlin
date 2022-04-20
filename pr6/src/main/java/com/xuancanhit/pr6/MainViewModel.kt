package com.xuancanhit.pr6

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    // Create a LiveData with a random number
    val number :MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }

    fun doRandomNumber() {
        RandomNumber.setRandomNumber()
        number.value = RandomNumber.getRandomNumber()
    }
}