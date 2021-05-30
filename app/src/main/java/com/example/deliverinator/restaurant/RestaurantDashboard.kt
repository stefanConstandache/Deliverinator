package com.example.deliverinator.restaurant

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.deliverinator.Login
import com.example.deliverinator.MENU_ITEMS
import com.example.deliverinator.ORDERS
import com.example.deliverinator.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RestaurantDashboard : AppCompatActivity() {
    private lateinit var mNavigationView: BottomNavigationView
    private lateinit var mFrameLayout: FrameLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAccountFragment: AccountFragment
    private lateinit var mMenuFragment: MenuFragment
    private lateinit var mOrdersFragment: OrdersFragment
    private lateinit var mEmail: String
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mDBListener: ValueEventListener
    private var mChannelId: String = "CHANNEL_ID"
    private val mNotificationId: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_dashboard)

        mNavigationView = findViewById(R.id.restaurant_dashboard_navigationView)
        mFrameLayout = findViewById(R.id.restaurant_dashboard_frame)

        mAuth = FirebaseAuth.getInstance()
        mEmail = mAuth.currentUser?.email!!.replace("[@.]".toRegex(), "_")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mEmail).child(ORDERS)

        mDBListener = mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                createNotificationChannel()
                sendNotification()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RestaurantDashboard, error.message, Toast.LENGTH_SHORT).show()
            }
        })

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

        mDatabaseRef.removeEventListener(mDBListener)
        MenuFragment.DBListener?.let {
            databaseRefMenu.removeEventListener(it)
        }
        OrdersFragment.DBListener?.let {
            databaseRefOrders.removeEventListener(it)
        }
        mAuth.signOut()

        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        finish()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(mChannelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.splash_image)
        val builder = NotificationCompat.Builder(this, mChannelId)
            .setSmallIcon(R.drawable.splash_image)
            .setContentTitle(getString(R.string.new_orders_received))
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).run {
            notify(mNotificationId, builder.build())
        }
    }
}