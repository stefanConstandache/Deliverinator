package com.example.deliverinator.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.deliverinator.Login
import com.example.deliverinator.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminDashboard : AppCompatActivity() {
    private lateinit var mNavigationView: BottomNavigationView
    private lateinit var mFrameLayout: FrameLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAddFragment: AddFragment
    private lateinit var mDeleteFragment: DeleteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        mNavigationView = findViewById(R.id.admin_dashboard_navigationView)
        mFrameLayout = findViewById(R.id.admin_dashboard_frame)

        mAuth = FirebaseAuth.getInstance()

        mAddFragment = AddFragment()
        mDeleteFragment = DeleteFragment()

        mNavigationView.selectedItemId = R.id.admin_dashboard_menu_add
        setFragment(mAddFragment)

        mNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.admin_dashboard_menu_add -> {
                    setFragment(mAddFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.admin_dashboard_menu_delete -> {
                    setFragment(mDeleteFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.admin_dashboard_frame, fragment)
        fragmentTransaction.commit()
    }

    fun launchLogin(view: View) {
        mAuth.signOut()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        finish()
    }
}