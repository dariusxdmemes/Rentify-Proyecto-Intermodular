package com.example.rentify_proyecto_intermodular.data.api

import android.util.Log
import com.example.rentify_proyecto_intermodular.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

const val HOST = "10.0.2.2" // The PC
//const val HOST = "raspberrypi.local" // The Raspberry
const val PORT = 8000

val client: OkHttpClient = OkHttpClient()

/**
 * Asks the API for a user with the specified email. DOES NOT VALIDATE THE EMAIL STRING!!!
 * @param email The email of the user to be requested
 * @return Returns a User object
 * @throws IOException
 */
suspend fun getUserByEmail(email: String): User {
    return withContext(Dispatchers.IO){
        var user = User(0, "", "")

        val encodedEmail = URLEncoder.encode(email, "UTF-8")

        val request = Request.Builder()
            .url("http://$HOST:$PORT/filter/Users?email=$encodedEmail")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                throw IOException("Network Error")
            }

            val body = response.body?.string()
            if (body == null) throw IOException("Response body is null")
            else {
                val jsonArray = JSONArray(body)
                if (jsonArray.length() == 0) user = User(0, "", "")
                else {
                    val jsonObject = jsonArray.getJSONObject(0)
                    user = User(
                        id = jsonObject.getInt("id"),
                        email = jsonObject.getString("email"),
                        password = jsonObject.getString("password")
                    )
                }
            }
        }

        user
    }
}
