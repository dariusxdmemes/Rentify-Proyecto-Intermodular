package com.example.rentify_proyecto_intermodular.data.api

import android.content.Context
import android.util.Log
import android.widget.Toast
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
 * @return Returns the information of the user on successful login, and `null` on incorrect credentials.
 * @throws IOException Throws `IOException` in case of network error
 */

suspend fun login(email: String, password: String): User? {
    try {
        return withContext(Dispatchers.IO) {
            var user: User? = null

            val jsonBody = """
                {
                    "email": "$email",
                    "password": "$password"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/login")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: throw IOException("Empty body")
                    val jsonObject = JSONObject(body)

                    user = User(
                        jsonObject.getInt("id_user"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("telephone"),
                        jsonObject.getString("email"),
                        ""
                    )

                } else {
                    if (response.code == 401) {
                        user = null
                    } else if (response.code == 400) {
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

suspend fun validateOwner(user: User?): Boolean {
    return withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://$HOST:$PORT/owners?user_fk=${user?.id}")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return@withContext false
                }
                val jsonArray = JSONArray(responseBody)

                return@withContext jsonArray.length() > 0
            }
        } catch (e: Exception) {
            Log.e("ValidateOwnerError", "Excepción: ${e.message}")
            return@withContext false
        }
    }
}

suspend fun validateTenant(user: User?): Boolean {
    return withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://$HOST:$PORT/renters?user_fk=${user?.id}")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {

                    return@withContext false
                }

                val jsonArray = JSONArray(responseBody)

                return@withContext jsonArray.length() > 0
            }
        } catch (e: Exception) {
            Log.e("ValidateTenantError", "Excepción: ${e.message}")
            return@withContext false
        }
    }
}





/**
 * Registers a user in the database
 * @param user The `User` object that needs to be registered. ID field is ignored.
 * @return An status code. 0: success. 1: duplicated email. 2: unexpected error.
 */

suspend fun registerUser(user: User): String {
    return try {
        withContext(Dispatchers.IO) {
            val jsonBody = """
                {
                    "name": "${user.firstName}",
                    "surname": "${user.lastName}",
                    "email": "${user.email}",
                    "telephone": "${user.phoneNumber}",
                    "password": "${user.password}"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("http://$HOST:$PORT/users")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                when {
                    response.isSuccessful -> "Usuario registrado correctamente"
                    response.code == 409 -> "Email o telefono ya existentes"
                    response.code == 400 -> "Datos inválidos enviados al servidor"
                    response.code == 500 -> "Error interno del servidor"
                    else -> "Error desconocido: ${response.code}"
                }
            }
        }
    } catch (e: IOException) {
        "Error de red: ${e.message}"
    } catch (e: Exception) {
        "Error inesperado: ${e.message}"
    }
}

/*
// =========================
// ===== API GUILLERMO =====
// =========================

/**
 * Asks the API for a user with the specified email. DOES NOT VALIDATE THE EMAIL STRING NOR HASHES THE PASSWORD!!!
 * @param email The email of the user to be requested
 * @return Returns a User object
 * @throws IOException
 */
suspend fun getUserByEmail(email: String): User {
    return withContext(Dispatchers.IO){
        var userId = 0
        var userNif = ""
        var userFirstName = ""
        var userLastName = ""
        var userPhoneNumber = ""
        var userEmail = ""
        var userPassword = ""
        var userAddressId = 0

        val encodedEmail = URLEncoder.encode(email, "UTF-8")

        val request = Request.Builder()
            .url("http://$HOST:$PORT/$USER_TABLE?$EMAIL_FIELD=$encodedEmail")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                throw IOException("Network Error")
            }

            val body = response.body?.string()
            if (body == null) throw IOException("Response body is null")
            else {
                val jsonArray = JSONArray(body)
                if (jsonArray.length() == 0) user = User(0, "", "", "", "", "", "", 0) // Represents an inexistent User
                else {
                    val jsonObject = jsonArray.getJSONObject(0)

                    userId = jsonObject.getInt(ID_FIELD)
                    userFirstName = jsonObject.getString(FIRST_NAME_FIELD)
                    userLastName = jsonObject.getString(LAST_NAME_FIELD)
                    userPhoneNumber = jsonObject.getString(PHONE_NUMBER_FIELD)
                    userEmail = jsonObject.getString(EMAIL_FIELD)
                    userPassword = jsonObject.getString(PASSWORD_FIELD)
                    userAddressId = jsonObject.getInt(ADDRESS_FIELD)
                    userNif = jsonObject.getString(NIF_FIELD)
                }
            }
        }

        User()
    }
}

/**
 * Asks the API to insert a new user into the database (for register)
 * @param user The User object that needs to be inserted
 * @return Returns a status code. 0 = Succes; 1 = Failure; -1 = Method error;
 */
/*
TODO
    - Hash password
 */
suspend fun insertUser(user: User): Int {
    return withContext(Dispatchers.IO){
        var statusCode = -1

        val encodedEmail = URLEncoder.encode(user.email, "UTF-8")

        val request = Request.Builder()
            .url("http://$HOST:$PORT/insert/$USER_TABLE" +
                    "?$NIF_FIELD=${user.nif}" +
                    "&$FIRST_NAME_FIELD=${user.firstName}" +
                    "&$LAST_NAME_FIELD=${user.lastName}" +
                    "&$EMAIL_FIELD=${encodedEmail}" +
                    "&$PHONE_NUMBER_FIELD=${user.phoneNumber}" +
                    "&$PASSWORD_FIELD=${user.password}" +
                    "&$ADDRESS_FIELD=${user.addressId}")
            .post("".toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                statusCode = 1
                // TODO añadir códigos de error a la API
            }
            else {
                statusCode = 0
            }
        }

        statusCode
    }
}
*/