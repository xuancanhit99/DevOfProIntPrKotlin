package com.xuancanhit.pr6

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import kotlin.random.nextInt

class RandomNumberViewModel:ViewModel() {
    // Create a LiveData with a random number
    val currentRandomNumber:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun randomNum() {
        currentRandomNumber.value = Random.nextInt(0..50)
    }
}