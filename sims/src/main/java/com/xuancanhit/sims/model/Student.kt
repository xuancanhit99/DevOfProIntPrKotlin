package com.xuancanhit.sims.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Student(
    val email: String? = null,
    val name: String? = null,
    val no: String? = null,
    val img: String? = null,
    val dob: String? = null,
    val group: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val results: LearningResult? = null
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "name" to name,
            "no" to no,
            "img" to img,
            "dob" to dob,
            "group" to group,
            "phone" to phone,
            "gender" to gender,
            "results" to results
        )
    }
}