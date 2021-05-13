package com.example.deliverinator.admin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.deliverinator.Login
import com.example.deliverinator.R
import com.example.deliverinator.Utils.Companion.hideKeyboard
import com.example.deliverinator.Utils.Companion.isValidEmail
import com.example.deliverinator.Utils.Companion.isValidPassword
import com.example.deliverinator.Utils.Companion.isValidPhone
import com.example.deliverinator.restaurant.RestaurantDashboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterRestaurant : AppCompatActivity() {
    private lateinit var mFullName: EditText
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mConfirmPassword: EditText
    private lateinit var mPhone: EditText
    private lateinit var madmin_addBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_fragment_add)

        mFullName = findViewById(R.id.admin_add_full_name)
        mEmail = findViewById(R.id.admin_add_email)
        mPassword = findViewById(R.id.admin_add_password)
        mConfirmPassword = findViewById(R.id.admin_add_confirm_password)
        mPhone = findViewById(R.id.admin_add_phone)
        madmin_addBtn = findViewById(R.id.admin_add_button)
        mProgressBar = findViewById(R.id.progressBar)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
    }

    fun launchLoginActivity(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun registerRestaurant(view: View) {
        val fullName = mFullName.text.toString().trim()
        val email = mEmail.text.toString().trim()
        val password = mPassword.text.toString().trim()
        val confirmPassword = mConfirmPassword.text.toString().trim()
        val phone = mPhone.text.toString().trim()

        hideKeyboard()
    Toast.makeText(this, "asddasd", Toast.LENGTH_SHORT).show()
        if (TextUtils.isEmpty(fullName)) {
            mFullName.error = getString(R.string.register_empty_field)
            return
        }

        if (!isValidEmail(email)) {
            mEmail.error = getString(R.string.invalid_email)
            return
        }

        if (!isValidPhone(phone)) {
            mPhone.error = getString(R.string.register_invalid_phone)
            return
        }

        if (!isValidPassword(password)) {
            mPassword.error = getString(R.string.register_invalid_password)
            return
        }

        if (confirmPassword != password) {
            mConfirmPassword.error = getString(R.string.register_invalid_confirm_password)
            return
        }

        mProgressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                // Send verification link
                val user = mAuth.currentUser

                user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(this, R.string.verification_mail_sent, Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener { e ->
                        Toast.makeText(this, getString(R.string.link_not_sent) + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(this, R.string.user_registered, Toast.LENGTH_SHORT).show()
            }
        }.addOnSuccessListener {
            val user = mAuth.currentUser

            if (user != null) {
                val docRef: DocumentReference = mStore.collection("Users").document(user.uid)
                val userInfo = HashMap<String, Any>()

                userInfo["FullName"] = fullName
                userInfo["UserEmail"] = email
                userInfo["PhoneNumber"] = phone

                // 0 means admin, 1 means user, 2 means restaurant
                userInfo["UserType"] = "2"

                docRef.set(userInfo)

                mProgressBar.visibility = View.INVISIBLE

                val loginIntent = Intent(this, RestaurantDashboard::class.java)
                startActivity(loginIntent)

                finish()
            }
        }
    }

}