package com.example.rentify_proyecto_intermodular.data.api

import java.io.IOException


/**
 * Function that validates an email and password against the database
 * @param email The user's email
 * @param password The user's password in plain text
 * @return Returns an status code. 0: login succesfull. 1: Incorrect password. 2: User not found. 3: Database error. 4: Unknown error
 */
//TODO sanitize email
//TODO hash password
fun validateLoginData(email: String, password: String): Int{
    var returnCode = 0

    try {
        getUserByEmail(
            email = email,
            callBack = { user ->
                if (user.id == 0) returnCode = 2
                else {
                    if (user.password != password) returnCode = 1
                    else returnCode = 0
                }
            }
        )
    }
    catch (e: IOException){
        return 3
    }
    catch (e: Exception){
        return 4
    }

    return returnCode
}