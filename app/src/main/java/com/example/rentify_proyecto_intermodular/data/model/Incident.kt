package com.example.rentify_proyecto_intermodular.data.model


data class Incident (
    val id: Int,
    val issue: String,
    val description: String,
    val property_id: Int,
    val tenant: User?,
    val owner_id: Int,
)
