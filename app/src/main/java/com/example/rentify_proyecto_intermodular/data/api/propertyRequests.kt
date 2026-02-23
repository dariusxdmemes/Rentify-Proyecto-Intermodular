package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Property
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Registers a property in the database
 * @param property The `Property` object that needs to be registered. ID field is ignored.
 * @throws java.io.IOException on network error
 */
suspend fun registerProperty (property: Property) {
    try {
        return withContext(Dispatchers.IO) {
            var code = 1

            val jsonBody = """
                {
                    "address": "${property.address}",
                    "owner_fk": "${property.owner_fk}",
                    "ciudad": "${property.address}",
                    "pais": "${property.pais}",
                    "alquiler": "${property.alquiler}"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("$BASE_URL/property/register")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    code = 1
                } else {
                    code = 0 // success
                }
            }

            code
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}