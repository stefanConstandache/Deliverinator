package com.example.deliverinator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mLoginButton: Button
    private lateinit var mRegisterHere: TextView
    private lateinit var mForgotPassword: TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mEmail = findViewById(R.id.login_email_editText)
        mPassword = findViewById(R.id.login_password_editText)
        mLoginButton = findViewById(R.id.login_button)
        mRegisterHere = findViewById(R.id.login_register_here_textView)
        mForgotPassword = findViewById(R.id.login_forgot_password_textView)
        mProgressBar = findViewById(R.id.login_progressBar)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        val user = mAuth.currentUser
        val x = user?.email
        if (user != null && user.isEmailVerified) {
            val docRef: DocumentReference =
                mStore.collection("Users").document(user.uid)

            docRef.get().addOnSuccessListener { docSnap ->
                when {
                    docSnap.getString("UserType") == "0" -> {
                        val dashboardIntent = Intent(applicationContext, AdminDashboard::class.java)
                        startActivity(dashboardIntent)
                    }

                    docSnap.getString("UserType") == "1" -> {
                        val dashboardIntent = Intent(applicationContext, ClientDashboard::class.java)
                        startActivity(dashboardIntent)
                    }

                    docSnap.getString("UserType") == "2" -> {
                        val dashboardIntent = Intent(applicationContext, RestaurantDashboard::class.java)
                        startActivity(dashboardIntent)
                    }
                }
            }

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

        mProgressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val user = it.user

            when (user?.isEmailVerified) {
                true -> {
                    Toast.makeText(this, R.string.login_logged_in, Toast.LENGTH_SHORT).show()

                    mProgressBar.visibility = View.INVISIBLE

                    val docRef: DocumentReference =
                        mStore.collection("Users").document(user.uid)

                    docRef.get().addOnSuccessListener { docSnap ->
                        when {
                            docSnap.getString("UserType") == "0" -> {
                                val dashboardIntent = Intent(applicationContext, AdminDashboard::class.java)
                                startActivity(dashboardIntent)
                            }

                            docSnap.getString("UserType") == "1" -> {
                                val dashboardIntent = Intent(applicationContext, ClientDashboard::class.java)
                                startActivity(dashboardIntent)
                            }

                            docSnap.getString("UserType") == "2" -> {
                                val dashboardIntent = Intent(applicationContext, RestaurantDashboard::class.java)
                                startActivity(dashboardIntent)
                            }
                        }
                    }

                    finish()
                }

                false -> {
                    Toast.makeText(this, R.string.email_not_verified, Toast.LENGTH_SHORT).show()

                    mProgressBar.visibility = View.INVISIBLE
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, R.string.login_incorrect, Toast.LENGTH_SHORT).show()

            mProgressBar.visibility = View.INVISIBLE
        }
    }

    fun launchForgotPasswordDialog(view: View) {
        val mailField = EditText(view.context)
        val passwordResetDialog = AlertDialog.Builder(view.context)

        mailField.text = mEmail.text

        passwordResetDialog
            .setTitle(R.string.reset_password)
            .setMessage(R.string.enter_your_email)
            .setView(mailField)
            .setPositiveButton(R.string.send) { _, _ ->
                val mail = mailField.text.toString().trim()

                mAuth.sendPasswordResetEmail(mail)
                    .addOnSuccessListener {
                        Toast.makeText(this, R.string.reset_link_sent, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, getString(R.string.link_not_sent) + it.message, Toast.LENGTH_LONG)
                            .show()
                    }
            }
            .create()
            .show()
    }
}