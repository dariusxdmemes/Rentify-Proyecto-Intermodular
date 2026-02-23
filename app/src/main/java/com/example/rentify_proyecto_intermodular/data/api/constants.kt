package com.example.rentify_proyecto_intermodular.data.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient

const val HOST = "10.0.2.2" // The PC
//const val HOST = "raspberrypi.local" // The Raspberry
const val PORT = 8000
const val BASE_URL = "http://$HOST:$PORT"

// OTHER CONSTANTS
val jsonMediaType = "application/json; charset=utf-8".toMediaType()
val client: OkHttpClient = OkHttpClient()