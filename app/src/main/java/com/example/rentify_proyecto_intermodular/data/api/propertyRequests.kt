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

/**
 * Sends a request to update the property in the database that has the same ID as the passed property, with the passed information.
 * @param property The passed property to be updated.
 * @throws IOException on network error
 */
suspend fun updateProperty (property: Property) {
    withContext(Dispatchers.IO) {
        val jsonBody = """
            {
               "id": "${property.id}",
               "address": "${property.address}",
               "ciudad": "${property.ciudad}",
               "pais": "${property.pais}",
               "alquiler": ${property.alquiler}
            }
        """.trimIndent()
        val requestBody = jsonBody.toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("http://$HOST:$PORT/property/update")
            .put(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) return@use
            when (response.code) {
                400 -> throw IOException("Missing field")
                404 -> throw IOException("Property not found")
                else -> throw IOException("Unexpected error")
            }
        }
    }
}

/**
 * Sends a request to delete a property from the database.
 * @param propertyId The ID of the property to be deleted
 * @throws IOException on network error
 */
suspend fun deleteProperty (propertyId: Int) {
    withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://$HOST:$PORT/property/$propertyId")
            .delete()
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) return@use
            when (response.code) {
                404 -> throw IOException("Property not found")
                else -> throw IOException("Unexpected error")
            }
        }
    }
}
