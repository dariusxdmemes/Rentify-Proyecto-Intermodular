package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.data.model.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Checks the credentials against the API
 * @param email The user's email
 * @param password The user's password in plaintext
 * @param type The user's type, either "owner" o "tenant"
 * @return Returns the User object on successful login, and `null` on incorrect credentials.
 * @throws java.io.IOException Throws `IOException` in case of network error
 */
suspend fun login(email: String, password: String, type: UserType): User? {
    try {
        return withContext(Dispatchers.IO) {
            var user: User? = null

            val jsonBody = """
                {
                    "email": "$email",
                    "password": "$password",
                    "type": "${type.stringRepresentation}"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/login")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    when (response.code) {
                        401 -> {
                            user = null // invalid credentials
                        }

                        400 -> {
                            throw IOException("Missing field")
                        }

                        else -> {
                            throw IOException("Unexpected error")
                        }
                    }

                } else {
                    val body = response.body?.string() ?: throw IOException("Empty body")
                    val jsonObject = JSONObject(body)

                    val ownedProperties: List<Property>? =
                        if (jsonObject.has("ownedProperty") && !jsonObject.isNull("ownedProperty")) {

                            val jsonArray = jsonObject.getJSONArray("ownedProperty")

                            List(jsonArray.length()) { i ->
                                val propJson = jsonArray.getJSONObject(i)

                                Property(
                                    id = propJson.getInt("id"),
                                    address = propJson.getString("address"),
                                    owner_fk = propJson.getInt("owner_fk"),
                                    ciudad = propJson.getString("ciudad"),
                                    pais = propJson.getString("pais"),
                                    alquiler = propJson.getInt("alquiler")
                                )
                            }

                        } else null

                    val leasedProperty: Property? =
                        if (jsonObject.has("leasedProperty") && !jsonObject.isNull("leasedProperty")) {
                            val propJson = jsonObject.getJSONObject("leasedProperty")

                            Property(
                                id = propJson.getInt("id"),
                                address = propJson.getString("address"),
                                owner_fk = propJson.getInt("owner_fk"),
                                ciudad = propJson.getString("ciudad"),
                                pais = propJson.getString("pais"),
                                alquiler = propJson.getInt("alquiler")
                            )
                        } else null

                    user = User(
                        id = jsonObject.getInt("id"),
                        firstName = jsonObject.getString("first_name"),
                        lastName = jsonObject.getString("last_name"),
                        phoneNumber = jsonObject.getString("phone_number"),
                        email = jsonObject.getString("email"),
                        password = "",
                        ownedProperty = ownedProperties,
                        leasedProperty = leasedProperty,
                        type = UserType.entries.find {
                            it.stringRepresentation == jsonObject.getString("type")
                        } ?: throw IOException("Unrecognized user type")
                    )
                }
            }

            user
        }
    } catch (e: Exception) {
        throw e
    }
}

/**
 * Registers a user in the database
 * @param user The `User` object that needs to be registered. ID field is ignored.
 * @return An status code. 0: success. 1: duplicated email. 2: unexpected error.
 * @throws IOException on network error
 */
suspend fun registerUser(user: User): Int {
    try {
        return withContext(Dispatchers.IO) {
            var code = 2

            val jsonBody = """
                {
                    "first_name": "${user.firstName}",
                    "last_name": "${user.lastName}",
                    "email": "${user.email}",
                    "phone_number": "${user.phoneNumber}",
                    "password": "${user.password}",
                    "type": "${user.type.stringRepresentation}"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/register")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                code = if (!response.isSuccessful) {
                    if (response.code == 401) {
                        1 // duplicated email
                    } else {
                        2 // generic error
                    }
                } else {
                    0 // success
                }
            }

            code
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}

/**
 * Update a user in the database
 * @param (user, actualpassword, newpassword).
 * @return return de actual user with his information updated.
 */
suspend fun updateUser(user: User, actualpassword: String, newpassword: String ): User? {
    try {
        return withContext(Dispatchers.IO) {

            val jsonBody = """
                {
                    "id": "${user.id}",
                    "first_name": "${user.firstName}",
                    "last_name": "${user.lastName}",
                    "email": "${user.email}",
                    "phone_number": "${user.phoneNumber}",
                    "actualpassword": "$actualpassword",
                    "newpassword": "$newpassword"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/update/user")
                .put(requestBody)
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
                        ownedProperty = user.ownedProperty,
                        leasedProperty = user.leasedProperty,
                        type = user.type
                    )

                }
            }

        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}

/**
 * Delete a user in the database
 * @param id_user
 * @return An status code. 0: success. 1: user not found. 2: unexpected error.
 */
suspend fun deleteUser(id_user: Int): Int {
    try {
        return withContext(Dispatchers.IO) {
            var code = 1

            val request = Request.Builder()
                .url("http://$HOST:$PORT/users/$id_user")
                .delete()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    if (response.code == 404) {
                        code = 1 // user not found
                    } else {
                        code = 2 // generic error
                    }
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