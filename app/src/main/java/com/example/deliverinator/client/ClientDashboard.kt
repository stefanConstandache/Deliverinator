package com.example.deliverinator.client

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.deliverinator.Login
import com.example.deliverinator.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ClientDashboard : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mMenuNavigation: BottomNavigationView
    private lateinit var mMainFrame: FrameLayout
    private lateinit var mAccountFragment: AccountFragment
    private lateinit var mRestaurantsFragment: RestaurantsFragment
    private lateinit var mCartFragment: CartFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)

        mAuth = FirebaseAuth.getInstance()
        mMainFrame = findViewById(R.id.main_frame)
        mMenuNavigation = findViewById(R.id.client_dashboard_menu)
        mAccountFragment = AccountFragment()
        mRestaurantsFragment = RestaurantsFragment()
        mCartFragment = CartFragment()

        setFragment(mRestaurantsFragment)
        mMenuNavigation.selectedItemId = R.id.restaurant_menu

        mMenuNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.account_menu -> {
                    setFragment(mAccountFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.restaurant_menu -> {
                    setFragment(mRestaurantsFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.cart_menu -> {
                    setFragment(mCartFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }

    fun launchLogin(view: View) {
        mAuth.signOut()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        finish()
    }
}