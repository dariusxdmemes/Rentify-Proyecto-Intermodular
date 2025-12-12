package com.example.rentify_proyecto_intermodular.data.model

data class Property (
    val address: Address,
    val owner: Owner,
    val idufir: String,
    val tenants: List<Tenant>,
    val monthlyPrice: Double,
    val services: List<Service>
)
