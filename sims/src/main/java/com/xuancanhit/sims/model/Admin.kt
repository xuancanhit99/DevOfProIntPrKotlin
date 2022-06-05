package com.xuancanhit.sims.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Admin(
    val email: String? = null,
    val name: String? = null,
    val img: String? = null,
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "name" to name,
            "img" to img
        )
    }
}