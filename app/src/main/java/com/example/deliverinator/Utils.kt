package com.example.deliverinator

import android.text.TextUtils
import android.util.Patterns

val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$".toRegex()

class Utils {

    companion object {
        fun isValidEmail(email: String): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(password: String): Boolean =
            regex.matches(password)
    }
}