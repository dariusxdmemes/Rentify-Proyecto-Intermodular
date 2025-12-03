package com.example.rentify_proyecto_intermodular.data.api

import java.io.IOException


/**
 * Function that validates an email and password against the database
 * @param email The user's email
 * @param password The user's password in plain text
 * @return Returns an status code. 0: login succesfull. 1: Incorrect password. 2: User not found. 3: Database error. 4: Unknown error
 */
/*
TODO
    - sanitize email
    - hash password
 */
suspend fun validateLoginData(email: String, password: String): Int {
    try {
        val user = getUserByEmail(email = email)
        if (user.id == 0) return 2
        if (user.password != password) return 1
        return 0
    }
    catch (e: IOException){
        return 3
    }
    catch (e: Exception){
        return 4
    }
}
