package com.example.deliverinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.deliverinator.admin.AdminDashboard
import com.example.deliverinator.client.ClientDashboard
import com.example.deliverinator.restaurant.RestaurantDashboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SplashScreen : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        val user = mAuth.currentUser

        if (user != null && user.isEmailVerified) {
            val docRef = mStore.collection("Users").document(user.uid)

            docRef.get().addOnSuccessListener { docSnap ->
                val dashboardIntent: Intent? = when {
                    docSnap.getString("UserType") == "0" -> Intent(this, AdminDashboard::class.java)
                    docSnap.getString("UserType") == "1" -> Intent(this, ClientDashboard::class.java)
                    docSnap.getString("UserType") == "2" -> Intent(this, RestaurantDashboard::class.java)
                    else -> null
                }

                if (dashboardIntent != null) {
                    startActivity(dashboardIntent)
                    finish()
                } else {
                    mAuth.signOut()

                    val loginIntent = Intent(this, Login::class.java)
                    startActivity(loginIntent)
                    finish()

                    Toast.makeText(this, R.string.wrong_user_type, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}