package com.example.rentify_proyecto_intermodular.ui.register

fun isValidEmail(email: String): Boolean {
    return email.contains("@") && email.contains(".")
}

fun isValidPhone(phone: String): Boolean {
    return phone.all {
        it.isDigit()
    } && phone.length <= 9 && phone.isNotBlank()
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}