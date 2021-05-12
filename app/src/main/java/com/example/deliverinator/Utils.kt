package com.example.deliverinator

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$".toRegex()
val phoneRegex = "(\\+4|)?(07[0-8]{1}[0-9]{1}){1}?([0-9]{3}){2}\$".toRegex()
const val IMAGE_PICK_CODE = 1000
const val PERMISSION_CODE = 1001

class Utils {

    companion object {
        fun isValidEmail(email: String): Boolean =
            !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun isValidPassword(password: String): Boolean = passwordRegex.matches(password)

        fun isValidPhone(phone: String): Boolean = phoneRegex.matches(phone)

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}