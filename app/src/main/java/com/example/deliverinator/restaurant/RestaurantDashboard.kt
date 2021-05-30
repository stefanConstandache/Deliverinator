package com.example.deliverinator.restaurant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.deliverinator.Login
import com.example.deliverinator.MENU_ITEMS
import com.example.deliverinator.ORDERS
import com.example.deliverinator.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RestaurantDashboard : AppCompatActivity() {
    private lateinit var mNavigationView: BottomNavigationView
    private lateinit var mFrameLayout: FrameLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAccountFragment: AccountFragment
    private lateinit var mMenuFragment: MenuFragment
    private lateinit var mOrdersFragment: OrdersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_dashboard)

        mNavigationView = findViewById(R.id.restaurant_dashboard_navigationView)
        mFrameLayout = findViewById(R.id.restaurant_dashboard_frame)

        mAuth = FirebaseAuth.getInstance()

        mAccountFragment = AccountFragment()
        mMenuFragment = MenuFragment()
        mOrdersFragment = OrdersFragment()

        mNavigationView.selectedItemId = R.id.restaurant_dashboard_menu_menu
        setFragment(mMenuFragment)

        mNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.restaurant_dashboard_menu_account -> {
                    setFragment(mAccountFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.restaurant_dashboard_menu_menu -> {
                    setFragment(mMenuFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.restaurant_dashboard_menu_orders -> {
                    setFragment(mOrdersFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.restaurant_dashboard_frame, fragment)
        fragmentTransaction.commit()
    }

    fun launchLogin(view: View) {
        val email = mAuth.currentUser?.email!!.replace("[@.]".toRegex(), "_")
        val databaseRefMenu = FirebaseDatabase.getInstance().getReference(email).child(MENU_ITEMS)
        val databaseRefOrders = FirebaseDatabase.getInstance().getReference(email).child(ORDERS)

        databaseRefMenu.removeEventListener(MenuFragment.DBListener)
        databaseRefOrders.removeEventListener(OrdersFragment.DBListener)
        mAuth.signOut()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        finish()
    }
}