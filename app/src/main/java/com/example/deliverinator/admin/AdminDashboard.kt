package com.example.deliverinator.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.deliverinator.Login
import com.example.deliverinator.R
import com.google.firebase.auth.FirebaseAuth

class AdminDashboard : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        mAuth = FirebaseAuth.getInstance()
    }

    fun launchLogin(view: View) {
        mAuth.signOut()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        finish()
    }
}