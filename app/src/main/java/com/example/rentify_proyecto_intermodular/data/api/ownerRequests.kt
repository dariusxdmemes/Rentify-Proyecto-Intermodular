package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.data.model.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

/**
 * Returns the user owner of a property by his user id
 * @param ownerFK User ID
 * @return User or null if name and lastname are void
 * @throws java.io.IOException Network or unexpected error
 */
suspend fun getOwnerUser(ownerFK: Int): User? {
    try {
        return withContext(Dispatchers.IO) {

            val request = Request.Builder()
                .url("http://$HOST:$PORT/owner/$ownerFK")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected error")
                }

                val body = response.body?.string() ?: throw IOException("Empty body")
                val jsonObject = JSONObject(body)

                if (jsonObject.isNull("first_name") && jsonObject.isNull("last_name")) {
                    null
                } else {
                    User(
                        id = jsonObject.getInt("id"),
                        firstName = jsonObject.getString("first_name"),
                        lastName = jsonObject.getString("last_name"),
                        phoneNumber = jsonObject.getString("phone_number"),
                        email = jsonObject.getString("email"),
                        password = "",
                        ownedProperty = null,
                        leasedProperty = null,
                        type = UserType.entries.find {
                            it.stringRepresentation == jsonObject.getString("type")
                        } ?: throw IOException("Unrecognized user type")
                    )

                }
            }

        }
    } catch (e: Exception) {
        throw e
    }
}