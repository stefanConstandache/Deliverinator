package com.example.deliverinator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ClientDashboard : AppCompatActivity() {
    private lateinit var mHello: TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)

        mHello = findViewById(R.id.client_dashboard_textView)
        mAuth = FirebaseAuth.getInstance()

        mHello.text = "Hello client ${mAuth.currentUser?.email}"
    }

    fun launchLogin(view: View) {
        mAuth.signOut()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        finish()
    }
}