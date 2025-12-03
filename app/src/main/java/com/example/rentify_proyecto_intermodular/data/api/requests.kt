package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
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
const val ADDRESS_FIELD = "direction"

val client: OkHttpClient = OkHttpClient()

/**
 * Asks the API for a user with the specified email. DOES NOT VALIDATE THE EMAIL STRING!!!
 * @param email The email of the user to be requested
 * @return Returns a User object
 * @throws IOException
 */
suspend fun getUserByEmail(email: String): User {
    return withContext(Dispatchers.IO){
        var user = User(0, "", "", "", "", "", "", 0)

        val encodedEmail = URLEncoder.encode(email, "UTF-8")

        val request = Request.Builder()
            .url("http://$HOST:$PORT/filter/$USER_TABLE?$EMAIL_FIELD=$encodedEmail")
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
                    user = User(
                        id = jsonObject.getInt(ID_FIELD),
                        firstName = jsonObject.getString(FIRST_NAME_FIELD),
                        lastName = jsonObject.getString(LAST_NAME_FIELD),
                        phoneNumber = jsonObject.getString(PHONE_NUMBER_FIELD),
                        email = jsonObject.getString(EMAIL_FIELD),
                        password = jsonObject.getString(PASSWORD_FIELD),
                        addressId = jsonObject.getInt(ADDRESS_FIELD),
                        nif = jsonObject.getString(NIF_FIELD)
                    )
                }
            }
        }

        user
    }
}

/**
 * Asks the API to insert a new user into the database (for register)
 * @param user The User object that needs to be inserted
 * @return Returns a status code. 0 = Succes; 1 = Failure; -1 = Method error; Ask William to implement more error codes on the API!!!
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
