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
import com.example.deliverinator.*
import com.example.deliverinator.R
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
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(restaurantEmail!!).child(MENU_ITEMS)
        mItemsList = ArrayList()
        mCartItemsList = HashMap()

        mAdapter = ClientRestaurantMenuItemAdapter(this, mItemsList, this, mCartItemsList)

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
            val cartIntent = Intent(this, Cart::class.java)
            val bundle = bundleOf(CART_ITEMS to mCartItemsList)

            cartIntent.putExtra(CART_ITEMS, bundle)
            cartIntent.putExtra(EMAIL, intent.getStringExtra(EMAIL))
            startActivityForResult(cartIntent, 1)
        } else {
            Toast.makeText(this, "Your shopping cart is empty", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            mCartItemsList.clear()
            val items = data!!.getBundleExtra(MENU_ITEMS)!!.get(MENU_ITEMS) as HashMap<UploadMenuItem, Int>

            for (item in items) {
                for (position in 0 until mItemsList.size) {
                    if (item.key.itemName == mItemsList[position].itemName) {
                        mCartItemsList[mItemsList[position]] = item.value
                    }
                }
            }

            mAdapter.notifyDataSetChanged()
        }
    }
}