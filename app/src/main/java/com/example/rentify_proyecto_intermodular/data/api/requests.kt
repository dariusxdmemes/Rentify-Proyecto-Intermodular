package com.example.rentify_proyecto_intermodular.data.api

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
 * @return Returns the information of the user on successful login, and `null` on incorrect credentials.
 * @throws IOException Throws `IOException` in case of network error
 */

suspend fun login(email: String, password: String): User? {
    return withContext(Dispatchers.IO){
        var user: User? = null

        val jsonBody = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
        val requestBody = jsonBody.toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("http://$HOST:$PORT/user")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                if (response.code == 400) {
                    throw IOException("Missing field")
                }

                if (response.code == 401 || response.code == 404) {
                    user = null // User not found
                }
            }
            else {
                val body = response.body?.string() ?: throw IOException("Empty body")
                val jsonObject = JSONObject(body)

                user = User(
                    jsonObject.getInt(ID_FIELD),
                    jsonObject.getString(FIRST_NAME_FIELD),
                    jsonObject.getString(LAST_NAME_FIELD),
                    jsonObject.getString(PHONE_NUMBER_FIELD),
                    jsonObject.getString(EMAIL_FIELD),
                    jsonObject.getString(PASSWORD_FIELD)
                )
            }
        }

        user
    }
}



/**
 * Registers a user in the database
 * @param user The `User` object that needs to be registered. ID field is ignored.
 * @return An status code. 0: success. 1: duplicated email. 2: unexpected error.
 */

suspend fun registerUser(user: User): Int {
    return withContext(Dispatchers.IO){
        var code: Int = 2

        val jsonBody = """
            {
                "first_name": "${user.firstName}",
                "last_name": "${user.lastName}",
                "email": "${user.email}",
                "phone_number": "${user.phoneNumber}"
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
                if (response.code == 400) {
                    code = 2
                }

                if (response.code == 401) {
                    code = 1 // duplicated email
                }
            }
            else {
                code = 0
            }
        }

        code
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