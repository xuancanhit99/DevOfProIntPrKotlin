package com.xuancanhit.pr8

object Calculate {
    private var result: Int = 0

    fun setResult(select: Int, a: Int, b: Int, c: Int) {
        if (select == 0)
            result = (a + b + c) * 4
        else if (select == 1)
            result = (a * b + a * c + b * c) * 2
        else
            result = a * b * c
    }

    fun getResult(): Int {
        return result
    }
}