package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.data.model.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

/**
 * Returns a list with all the Images URLs
 * @return List of image URLs (can be empty)
 * @throws java.io.IOException on network or unexpected error
 */
suspend fun getImageUrls(): List<String> {
    return withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$BASE_URL/images")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body ?: throw IOException("Empty body")

                try {
                    val jsonArray = JSONArray(body.string())
                    return@use List (jsonArray.length()) { i -> jsonArray.getString(i) }
                }
                catch (e: JSONException){
                    throw IOException("Body is not JSON")
                }
            }

            when (response.code) {
                404 -> throw IOException("Not Found")
                400 -> throw IOException("Malformed request")
                else -> throw IOException("Unexpected error")
            }
        }
    }
}