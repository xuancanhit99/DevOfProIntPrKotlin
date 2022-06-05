package com.xuancanhit.sims.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class LearningResult (
    val math: String? = null,
    val eng: String? = null,
    val phy: String? = null,
    val info: String? = null,
    val pro: String? = null,
    val eco: String? = null,
    val phi: String? = null,
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "math" to math,
            "eng" to eng,
            "phy" to phy,
            "info" to info,
            "pro" to pro,
            "eco" to eco,
            "phone" to phi,
        )
    }
}