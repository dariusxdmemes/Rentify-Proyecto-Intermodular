package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.data.model.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException

/**
 * Returns the tenants of a property
 * @param propertyId Property ID
 * @return List of users (can be empty)
 * @throws java.io.IOException Network or unexpected error
 */
suspend fun getTenantsByProperty(propertyId: Int): List<User> {
    try {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("http://$HOST:$PORT/property/tenants/$propertyId")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected error")
                }

                val body = response.body?.string() ?: throw IOException("Empty body")
                val jsonArray = JSONArray(body)

                List(jsonArray.length()) { i ->
                    val u = jsonArray.getJSONObject(i)
                    User(
                        id = u.getInt("id"),
                        firstName = u.getString("first_name"),
                        lastName = u.getString("last_name"),
                        phoneNumber = u.getString("phone_number"),
                        email = u.getString("email"),
                        password = "",
                        ownedProperty = null,
                        leasedProperty = null,
                        type = UserType.TENANT
                    )
                }
            }
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}

/**
 * Binds a tenant to a specific property
 * @param idProperty The ID of the Property
 * @param idTenant The ID of the Tenant
 * @return A status code: 0 = success; 1 = tenant missing or already registered
 * @throws IOException on network error
 */
suspend fun bindTenantToProperty(idProperty: Int, tenantEmail: String): Int {
    try {
        return withContext(Dispatchers.IO) {
            var code = 1

            val jsonBody = """
                {
                    "property_fk": "$idProperty",
                    "email": "$tenantEmail"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("$BASE_URL/property/tenant/register")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    code = 0
                } else if (response.code == 404) {
                    code = 1
                } else {
                    throw IOException("Response unsuccessful")
                }
            }

            code
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error.")
    }
}