package com.example.rentify_proyecto_intermodular.data.model

data class Property(
    val id: Int,
    val address: String,
    val owner_fk: Int,
    val ciudad: String,
    val pais: String,
    val alquiler: Int
)
