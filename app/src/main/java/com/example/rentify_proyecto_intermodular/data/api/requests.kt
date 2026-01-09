package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Property
import com.example.rentify_proyecto_intermodular.data.model.Service
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

const val HOST = "10.0.2.2" // The PC
//const val HOST = "raspberrypi.local" // The Raspberry
const val PORT = 8000

// API TABLE NAMES
const val USER_TABLE = "users"

// USER API FIELDS
const val ID_FIELD = "id_user"
const val NIF_FIELD = "nif"
const val FIRST_NAME_FIELD = "name"
const val LAST_NAME_FIELD = "surname"
const val PHONE_NUMBER_FIELD = "telephone"
const val EMAIL_FIELD = "email"
const val PASSWORD_FIELD = "password"
const val ADDRESS_FIELD = "address_fk"

// OTHER CONSTANTS
val jsonMediaType = "application/json; charset=utf-8".toMediaType()

val client: OkHttpClient = OkHttpClient()


// =====================
// ===== API PABLO =====
// =====================

/**
 * Checks the credentials against the API
 * @param email The user's email
 * @param password The user's password in plaintext
 * @param type The user's type, either "owner" o "tenant"
 * @return Returns the User object on successful login, and `null` on incorrect credentials.
 * @throws IOException Throws `IOException` in case of network error
 */

suspend fun login(email: String, password: String, type: String): User? {
    try {
        return withContext(Dispatchers.IO){
            var user: User? = null

            val jsonBody = """
                {
                    "email": "$email",
                    "password": "$password",
                    "type": "$type"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/login")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful){
                    if (response.code == 401) {
                        user = null // invalid credentials
                    }
                    else if (response.code == 400) {
                        throw IOException("Missing field")
                    }
                    else {
                        throw IOException("Unexpected error")
                    }

                }
                else {
                    val body = response.body?.string() ?: throw IOException("Empty body")
                    val jsonObject = JSONObject(body)

                    if(type=="owner"){

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

                        user = User(
                            jsonObject.getInt("id"),
                            jsonObject.getString("first_name"),
                            jsonObject.getString("last_name"),
                            jsonObject.getString("phone_number"),
                            jsonObject.getString("email"),
                            "",
                            ownedProperties,
                            null
                        )
                    }
                    else if(type=="tenant"){

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
                            jsonObject.getInt("id"),
                            jsonObject.getString("first_name"),
                            jsonObject.getString("last_name"),
                            jsonObject.getString("phone_number"),
                            jsonObject.getString("email"),
                            "",
                            null,
                            leasedProperty
                        )
                    }
                    if(user != null && user.ownedProperty == null && user.leasedProperty == null){
                        //not owner or tenant
                        user = null
                    }

                }
            }

            user
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}



/**
 * Registers a user in the database
 * @param user The `User` object that needs to be registered. ID field is ignored.
 * @return An status code. 0: success. 1: duplicated email. 2: unexpected error.
 */

suspend fun registerUser(user: User): Int {
    try {
        return withContext(Dispatchers.IO){
            var code = 2

            val jsonBody = """
                {
                    "first_name": "${user.firstName}",
                    "last_name": "${user.lastName}",
                    "email": "${user.email}",
                    "phone_number": "${user.phoneNumber}",
                    "password": "${user.password}"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/register")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful){
                    if (response.code == 401) {
                        code = 1 // duplicated email
                    }
                    else {
                        code = 2 // generic error
                    }
                }
                else {
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
 * Returns the tenants of a property
 * @param propertyId Property ID
 * @return List of users (can be empty)
 * @throws IOException Network or unexpected error
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
                        leasedProperty = null
                    )
                }
            }
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}
/**
 * Returns the services of a property
 * @param propertyId Property ID
 * @return Service or null if not exists
 * @throws IOException Network or unexpected error
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
