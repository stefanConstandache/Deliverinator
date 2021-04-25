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

class Register : AppCompatActivity() {
    lateinit var mFullName: EditText
    lateinit var mEmail: EditText
    lateinit var mPassword: EditText
    lateinit var mConfirmPassword: EditText
    lateinit var mPhone: EditText
    lateinit var mRegisterBtn: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var mLoginBtn: TextView
    lateinit var mProgressBar: ProgressBar

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
            mFullName.error = "Field cannot be empty"
            return
        }

        if (!isValidEmail(email)) {
            mEmail.error = "Invalid email"
            return
        }

        if (!isValidPhone(phone)) {
            mPhone.error = "Invalid phone number"
        }

        if (!isValidPassword(password)) {
            mPassword.error = "Password must be 8 characters long and contain at least one number and one uppercase"
            return
        }

        if (confirmPassword != password) {
            mConfirmPassword.error = "Passwords must match"
            return
        }

        mProgressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Registration Completed", Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE

                val dashboardIntent = Intent(this, Dashboard::class.java)
                startActivity(dashboardIntent)
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE
            }
        }
    }
}