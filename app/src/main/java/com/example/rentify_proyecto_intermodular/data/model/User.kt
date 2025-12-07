package com.example.rentify_proyecto_intermodular.data.model

data class User (
    val id: Int,
    val nif: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val addressId: Int
)
