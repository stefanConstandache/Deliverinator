package com.example.deliverinator

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.deliverinator.Utils.Companion.isValidEmail
import com.example.deliverinator.Utils.Companion.isValidPassword
import com.example.deliverinator.Utils.Companion.isValidPhone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var mFullName: EditText
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mConfirmPassword: EditText
    private lateinit var mPhone: EditText
    private lateinit var mRegisterBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLoginBtn: TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFullName = findViewById(R.id.register_full_name)
        mEmail = findViewById(R.id.register_email)
        mPassword = findViewById(R.id.register_password)
        mConfirmPassword = findViewById(R.id.register_confirm_password)
        mPhone = findViewById(R.id.register_phone)
        mRegisterBtn = findViewById(R.id.register_button)
        mLoginBtn = findViewById(R.id.register_login_here)
        mProgressBar = findViewById(R.id.progressBar)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        if (mAuth.currentUser != null) {
            val dashboardIntent = Intent(this, ClientDashboard::class.java)
            startActivity(dashboardIntent)
            finish()
        }
    }

    fun launchLoginActivity(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun launchDashBoard(view: View) {
        val fullName = mFullName.text.toString().trim()
        val email = mEmail.text.toString().trim()
        val password = mPassword.text.toString().trim()
        val confirmPassword = mConfirmPassword.text.toString().trim()
        val phone = mPhone.text.toString().trim()

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

                Toast.makeText(this, R.string.registration_completed, Toast.LENGTH_SHORT).show()
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
                userInfo["UserType"] = 1

                docRef.set(userInfo)

                mProgressBar.visibility = View.INVISIBLE

                val loginIntent = Intent(this, Login::class.java)
                startActivity(loginIntent)

                finish()
            }
        }
    }
}