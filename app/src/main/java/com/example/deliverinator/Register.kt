package com.example.deliverinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.deliverinator.Utils.Companion.isValidEmail
import com.example.deliverinator.Utils.Companion.isValidPassword
import com.example.deliverinator.Utils.Companion.isValidPhone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class Register : AppCompatActivity() {
    lateinit var mFullName: EditText
    lateinit var mEmail: EditText
    lateinit var mPassword: EditText
    lateinit var mConfirmPassword: EditText
    lateinit var mPhone: EditText
    lateinit var mRegisterBtn: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var mStore: FirebaseFirestore
    lateinit var mLoginBtn: TextView
    lateinit var mProgressBar: ProgressBar
    lateinit var mRadioGroup: RadioGroup

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
        mRadioGroup = findViewById(R.id.radioGroup)

        if (mAuth.currentUser != null) {
            val dashboardIntent = Intent(this, Dashboard::class.java)
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
                Toast.makeText(this, getString(R.string.registration_completed), Toast.LENGTH_SHORT).show()

                val user = mAuth.currentUser
                if (user != null) {
                    val radioButton: RadioButton = findViewById(mRadioGroup.checkedRadioButtonId)
                    val docRef: DocumentReference = mStore.collection("Users").document(user.uid)
                    val userInfo = HashMap<String, Any>()

                    userInfo["FullName"] = fullName
                    userInfo["UserEmail"] = email
                    userInfo["PhoneNumber"] = phone

                    // 0 means admin, 1 means user, 2 means restaurant
                    if (radioButton.text == "Register as Client"){
                        userInfo["UserType"] = "1"
                    } else {
                        userInfo["UserType"] = "2"
                    }

                    docRef.set(userInfo)
                }

                mProgressBar.visibility = View.INVISIBLE

                val loginIntent = Intent(this, Login::class.java)
                startActivity(loginIntent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.user_registered), Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE
            }
        }
    }
}