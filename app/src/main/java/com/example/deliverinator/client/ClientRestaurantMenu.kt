package com.example.deliverinator.client

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.CART_ITEMS
import com.example.deliverinator.EMAIL
import com.example.deliverinator.R
import com.example.deliverinator.UploadMenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.client_restaurant_menu.*

class ClientRestaurantMenu : AppCompatActivity(),
    ClientRestaurantMenuItemAdapter.OnItemClickListener {
    private lateinit var mAdapter: ClientRestaurantMenuItemAdapter
    private lateinit var mItemsList: ArrayList<UploadMenuItem>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mDBListener: ValueEventListener
    private lateinit var mCartItemsList: HashMap<UploadMenuItem, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_restaurant_menu)

        val intent = intent
        val restaurantEmail = intent.getStringExtra(EMAIL)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(restaurantEmail!!)
        mItemsList = ArrayList()
        mCartItemsList = HashMap()

        mAdapter = ClientRestaurantMenuItemAdapter(this, mItemsList, this)

        client_restaurant_menu_recyclerView.adapter = mAdapter
        client_restaurant_menu_recyclerView.layoutManager = LinearLayoutManager(this)
        client_restaurant_menu_recyclerView.setHasFixedSize(true)

        mDBListener = mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mItemsList.clear()

                for (postSnapshot in snapshot.children) {
                    val upload = postSnapshot.getValue(UploadMenuItem::class.java)

                    if (upload != null) {
                        upload.key = postSnapshot.key

                        if (upload.isAvailable) {
                            mItemsList.add(upload)
                        }
                    }
                }

                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ClientRestaurantMenu, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onAddClick(position: Int, textView: TextView) {
        val quantity = textView.text.toString().toInt() + 1

        textView.text = "$quantity"
        mCartItemsList[mItemsList[position]] = quantity
    }

    override fun onRemoveClick(position: Int, textView: TextView) {
        if (textView.text == "1") {
            textView.text = "0"
            mCartItemsList.remove(mItemsList[position])
        } else if (textView.text != "0") {
            val quantity = textView.text.toString().toInt() - 1

            textView.text = "$quantity"
            mCartItemsList[mItemsList[position]] = quantity
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(R.string.leave_restaurant)
            .setPositiveButton(R.string.yes) { _, _ ->
                finish()
            }
            .setNegativeButton(R.string.no, null)
            .create()
            .show()
    }

    fun launchCart(view: View) {
        if (mCartItemsList.isNotEmpty()) {
            val intent = Intent(this, Cart::class.java)
            val bundle = bundleOf(CART_ITEMS to mCartItemsList)


            intent.putExtra(CART_ITEMS, bundle)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Your shopping cart is empty", Toast.LENGTH_SHORT).show()
        }
    }
}