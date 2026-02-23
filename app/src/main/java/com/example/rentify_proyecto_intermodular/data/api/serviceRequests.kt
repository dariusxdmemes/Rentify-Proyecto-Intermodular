package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

/**
 * Returns the services of a property
 * @param propertyId Property ID
 * @return Service or null if not exists
 * @throws java.io.IOException Network or unexpected error
 */
suspend fun getServicesByProperty(propertyId: Int): Service? {
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

                if (jsonObject.isNull("included") && jsonObject.isNull("excluded")) {
                    null
                } else {
                    Service(
                        included = jsonObject.getString("included"),
                        excluded = jsonObject.getString("excluded")
                    )
                }
            }
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}