package com.xuancanhit.pr8

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    // Create a LiveData
    val res = MutableLiveData<Int>()

    fun doCalculate(select: Int, a: Int, b: Int, c: Int) {
        Calculate.setResult(select, a, b, c)
        res.value = Calculate.getResult()
    }
}