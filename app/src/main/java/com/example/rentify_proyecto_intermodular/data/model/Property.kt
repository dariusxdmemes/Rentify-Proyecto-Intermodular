package com.example.rentify_proyecto_intermodular.data.model

//data class Property (
//    val address: Address,
//    val owner: Owner,
//    val idufir: String,
//    val tenants: List<Tenant>,
//    val monthlyPrice: Double,
//    val services: List<Service>
//)

//adaptacion a los headers de la base de datos que devuelve actualmente
data class Property(
    val id: Int,
    val address: String,
    val owner_fk: Int,
    val ciudad: String,
    val pais: String,
    val alquiler: Int
)
