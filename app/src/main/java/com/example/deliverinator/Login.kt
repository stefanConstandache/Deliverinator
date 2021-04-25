package com.example.deliverinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.deliverinator.Utils.Companion.isValidEmail
import com.example.deliverinator.Utils.Companion.isValidPassword
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var mEmail: EditText
    lateinit var mPassword: EditText
    lateinit var mLoginButton: Button
    lateinit var mRegisterHere: TextView
    lateinit var mProgressBar: ProgressBar
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mEmail = findViewById(R.id.login_email_editText)
        mPassword = findViewById(R.id.login_password_editText)
        mLoginButton = findViewById(R.id.login_button)
        mRegisterHere = findViewById(R.id.login_register_here_textView)
        mProgressBar = findViewById(R.id.login_progressBar)
        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            val dashboardIntent = Intent(applicationContext, Dashboard::class.java)
            startActivity(dashboardIntent)
            finish()
        }

    }

    fun launchRegisterActivity(view: View) {
        intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    fun launchDashboard(view: View) {
        val email = mEmail.text.toString().trim()
        val password = mPassword.text.toString().trim()
        if (!isValidEmail(email)) {
            mEmail.error = "Invalid Email!"
            return
        }
        if (!isValidPassword(password)) {
            mPassword.error = "Invalid Password!"
            return
        }
        mProgressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { 
            if (it.isSuccessful) {
                Toast.makeText(this, "Logged In!", Toast.LENGTH_SHORT).show()
                val dashboardIntent = Intent(applicationContext, Dashboard::class.java)
                startActivity(dashboardIntent)
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
            }
        }
        

    }
}