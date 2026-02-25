package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Returns the services of a property
 * @param propertyId Property ID
 * @return Service or null if not exists
 * @throws java.io.IOException Network or unexpected error
 */
suspend fun getServicesByProperty (propertyId: Int): Service? {
    try {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("http://$HOST:$PORT/property/services/$propertyId")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected error")
                }

                val body = response.body?.string() ?: throw IOException("Empty body")
                val jsonObject = JSONObject(body)

                if (jsonObject.isNull("included") || jsonObject.isNull("excluded")) null
                else Service(
                    included = jsonObject.getString("included"),
                    excluded = jsonObject.getString("excluded")
                )
            }
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}

/**
 * Sends a request to create a service on a specific property
 * @param propertyId the ID of the bound property
 * @param service the service object with the information
 * @throws IOException on network error
 */
suspend fun createServicesOnProperty (propertyId: Int, service: Service) {
    withContext(Dispatchers.IO) {
        val jsonBody = """
            {
                "property_fk": $propertyId,
                "included": "${service.included}",
                "excluded": "${service.excluded}"
            }
        """.trimIndent()
        val requestBody = jsonBody.toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$BASE_URL/services/create")
            .post(requestBody)
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
 * Sends a request to update the services of a property
 * @param propertyId The property of the service to be updated
 * @param newService The new service of the property
 * @throws IOException On network error
 */
suspend fun updateServicesOfProperty (propertyId: Int, newService: Service) {
    withContext(Dispatchers.IO) {
        val jsonBody = """
            {
                "property_fk": $propertyId,
                "included": "${newService.included}",
                "excluded": "${newService.excluded}"
            }
        """.trimIndent()
        val requestBody = jsonBody.toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$BASE_URL/services/update")
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
