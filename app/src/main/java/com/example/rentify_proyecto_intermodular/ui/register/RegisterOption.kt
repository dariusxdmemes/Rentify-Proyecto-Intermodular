package com.example.rentify_proyecto_intermodular.ui.register

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState

data class RegisterOption(
    var fieldLabel: String,
    var fieldState: MutableState<String>,
    val fieldKeyboardOption: KeyboardOptions
)
