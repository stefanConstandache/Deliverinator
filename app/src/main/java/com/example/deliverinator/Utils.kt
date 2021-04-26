package com.example.deliverinator

import android.text.TextUtils
import android.util.Patterns

val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$".toRegex()
val phoneRegex = "(\\+4|)?(07[0-8]{1}[0-9]{1}){1}?([0-9]{3}){2}\$".toRegex()

class Utils {

    companion object {
        fun isValidEmail(email: String): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun isValidPassword(password: String): Boolean = passwordRegex.matches(password)

        fun isValidPhone(phone: String): Boolean = phoneRegex.matches(phone)
    }
}