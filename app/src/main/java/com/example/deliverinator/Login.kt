package com.example.deliverinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.deliverinator.Utils.Companion.isValidEmail
import com.example.deliverinator.Utils.Companion.isValidPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    lateinit var mEmail: EditText
    lateinit var mPassword: EditText
    lateinit var mLoginButton: Button
    lateinit var mRegisterHere: TextView
    lateinit var mProgressBar: ProgressBar
    lateinit var mAuth: FirebaseAuth
    lateinit var mStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mEmail = findViewById(R.id.login_email_editText)
        mPassword = findViewById(R.id.login_password_editText)
        mLoginButton = findViewById(R.id.login_button)
        mRegisterHere = findViewById(R.id.login_register_here_textView)
        mProgressBar = findViewById(R.id.login_progressBar)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        if (mAuth.currentUser != null) {
            val dashboardIntent = Intent(applicationContext, Dashboard::class.java)
            startActivity(dashboardIntent)
            finish()
        }
    }

    fun launchRegisterActivity(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    fun launchDashboard(view: View) {
        val email = mEmail.text.toString().trim()
        val password = mPassword.text.toString().trim()

        if (!isValidEmail(email)) {
            mEmail.error = getString(R.string.invalid_email)
            return
        }

        if (!isValidPassword(password)) {
            mPassword.error = getString(R.string.invalid_password)
            return
        }

        mProgressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, getString(R.string.login_logged_in), Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE

                val user = mAuth.currentUser
                if (user != null) {
                    val docRef: DocumentReference =
                        mStore.collection("Users").document(user.uid)
                    docRef.get().addOnSuccessListener { docSnap ->
                        if (docSnap.getString("UserType") != "0") {
                            val dashboardIntent = Intent(applicationContext, AdminDashboard::class.java)
                            startActivity(dashboardIntent)
                        } else if (docSnap.getString("UserType") != "1") {
                            val dashboardIntent = Intent(applicationContext, Dashboard::class.java)
                            startActivity(dashboardIntent)
                        } else if (docSnap.getString("UserType") != "2") {
                            val dashboardIntent = Intent(applicationContext, RestaurantDashboard::class.java)
                            startActivity(dashboardIntent)
                        }
                    }
                }

                finish()
            } else {
                Toast.makeText(this, getString(R.string.login_incorrect), Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE
            }
        }
    }
}