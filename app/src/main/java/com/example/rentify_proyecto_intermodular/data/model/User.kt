package com.example.rentify_proyecto_intermodular.data.model

import org.json.JSONArray
import org.json.JSONObject
enum class Type (val value: String) {
    TENANT("tenant"),
    OWNER("owner")
}

data class User (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val ownedProperty: List<Property>? = null,
    val leasedProperty: Property? = null,
    val type: Type? = null
)
