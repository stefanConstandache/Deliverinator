package com.example.deliverinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.client.ClientAddItemAdapter
import com.example.deliverinator.client.RestaurantItemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_client_add_items_from_menu.*

class ActivityClientAddItemsFromMenu : AppCompatActivity(),
    RestaurantItemAdapter.OnItemClickListener, ClientAddItemAdapter.OnItemClickListener {
    private lateinit var mAdapter: ClientAddItemAdapter
    private lateinit var mItemsList: ArrayList<UploadMenuItem>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mDBListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_add_items_from_menu)

        val intent: Intent = intent
        val restaurantEmail = intent.getStringExtra(EMAIL)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(restaurantEmail!!)
        mItemsList = ArrayList()

        mAdapter = ClientAddItemAdapter(this, mItemsList, this)

        client_add_item_recyclerView.adapter = mAdapter
        client_add_item_recyclerView.layoutManager = LinearLayoutManager(this)
        client_add_item_recyclerView.setHasFixedSize(true)

        mDBListener = mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mItemsList.clear()

                for (postSnapshot in snapshot.children) {
                    val upload = postSnapshot.getValue(UploadMenuItem::class.java)

                    if (upload != null) {
                        upload.key = postSnapshot.key
                        mItemsList.add(upload)
                    }
                }

                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivityClientAddItemsFromMenu, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(position: Int, view: View?) {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
    }
}