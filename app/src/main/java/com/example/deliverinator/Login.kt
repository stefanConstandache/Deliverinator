package com.example.deliverinator

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.deliverinator.Utils.Companion.isValidEmail
import com.example.deliverinator.Utils.Companion.isValidPassword
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var mEmail: EditText
    lateinit var mPassword: EditText
    lateinit var mLoginButton: Button
    lateinit var mRegisterHere: TextView
    lateinit var mForgotPassword: TextView
    lateinit var mProgressBar: ProgressBar
    lateinit var mAuth: FirebaseAuth

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

        mProgressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, R.string.login_logged_in, Toast.LENGTH_SHORT).show()

                val dashboardIntent = Intent(applicationContext, Dashboard::class.java)
                startActivity(dashboardIntent)

                mProgressBar.visibility = View.INVISIBLE
            } else {
                Toast.makeText(this, R.string.login_incorrect, Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE
            }
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

                mAuth
                    .sendPasswordResetEmail(mail)
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