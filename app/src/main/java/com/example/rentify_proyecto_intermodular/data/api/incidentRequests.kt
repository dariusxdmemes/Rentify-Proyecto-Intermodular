package com.example.rentify_proyecto_intermodular.data.api

import com.example.rentify_proyecto_intermodular.data.model.Incident
import com.example.rentify_proyecto_intermodular.data.model.User
import com.example.rentify_proyecto_intermodular.data.model.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException

/**
 * Returns the incidents of a property
 * @param propertyId Property ID
 * @return List of incidents (can be empty)
 * @throws java.io.IOException Network or unexpected error
 */
suspend fun getIncidentsByProperty(propertyId: Int): List<Incident> {
    try {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("http://$HOST:$PORT/property/incidents/$propertyId")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected error")
                }

                val body = response.body?.string() ?: throw IOException("Empty body")
                val jsonArray = JSONArray(body)

                List(jsonArray.length()) { i ->
                    val incident = jsonArray.getJSONObject(i)
                    val tenant: User? =
                        if (incident.has("tenant")) {
                            val propJson = incident.getJSONObject("tenant")
                            User(
                                id = propJson.getInt("id"),
                                firstName = propJson.getString("first_name"),
                                lastName = propJson.getString("last_name"),
                                phoneNumber = propJson.getString("phone_number"),
                                email = propJson.getString("email"),
                                password = "",
                                ownedProperty = null,
                                leasedProperty = null,
                                type = UserType.TENANT
                            )
                        } else null
                    Incident(
                        id = incident.getInt("id"),
                        issue = incident.getString("issue"),
                        description = incident.getString("description"),
                        property_id = incident.getInt("property_id"),
                        tenant = tenant,
                        owner_id = incident.getInt("owner_id")

                    )
                }
            }
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error")
    }
}


/**
 * Registers a property in the database
 * @param incident The `Incident` object that needs to be registered. ID field is ignored.
 * @throws IOException on network error
 */
suspend fun createIncident (incident: Incident) {
    if (incident.tenant == null){
        throw IOException("Incident's tenant is null.")
    }

    try {
        return withContext(Dispatchers.IO) {
            val jsonBody = """
                {
                    "asunto": "${incident.issue}",
                    "descrip": "${incident.description}",
                    "id_owner": "${incident.owner_id}",
                    "id_tenant": "${incident.tenant.id}",
                    "id_property": "${incident.property_id}"
                }
            """.trimIndent()
            val requestBody = jsonBody.toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("$BASE_URL/incidents/create")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Response unsuccessful.")
                }
            }
        }
    } catch (e: Exception) {
        throw IOException("Unexpected error.")
    }
}