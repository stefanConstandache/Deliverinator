package com.example.deliverinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.client.ClientAddItemAdapter
import com.example.deliverinator.client.ClientAddItemItem
import com.example.deliverinator.client.RestaurantItem
import com.example.deliverinator.client.RestaurantItemAdapter
import com.example.deliverinator.restaurant.UploadMenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.client_fragment_restaurants.view.*
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.*

class ActivityClientAddItemsFromMenu : AppCompatActivity() {
    lateinit var adapter:ClientAddItemAdapter
    var database = FirebaseFirestore.getInstance()
    lateinit var itemsList:ArrayList<ClientAddItemItem>
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var dbListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_add_items_from_menu)
        val intent:Intent = intent
        val restaurantName = intent.getStringExtra(NAME)



        databaseRef = FirebaseDatabase.getInstance().getReference(auth.currentUser?.uid!!)

        //itemsList = generateDummyList(restaurantName)
        findViewById<RecyclerView>(R.id.client_add_item_recyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.client_add_item_recyclerView).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.client_add_item_recyclerView).setHasFixedSize(true)

        dbListener = databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemsList.clear()

                for (postSnapshot in snapshot.children) {
                    val upload = postSnapshot.getValue(ClientAddItemItem::class.java)

                    if (upload != null) {
                       // upload.key = postSnapshot.key
                        itemsList.add(upload)
                    }
                }

                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

    private fun generateDummyList(restaurantName:String?): ArrayList<ClientAddItemItem> {

        val list = ArrayList<ClientAddItemItem>()
        database.collection(RESTAURANTS).get()
            .addOnSuccessListener{ documents ->
                for(document in documents) {
                    val item = ClientAddItemItem(
                        document.getString(RESTAURANT_IMAGE), document.getString(
                            NAME
                        )!!,
                        document.getString(RESTAURANT_DESCRIPTION)!!
                    )
                    list.add(item)
                }
                adapter = ClientAddItemAdapter(this ,itemsList)
//                findViewById<RecyclerView>(R.id.client_add_item_recyclerView).adapter = adapter
//                findViewById<RecyclerView>(R.id.client_add_item_recyclerView).layoutManager = LinearLayoutManager(this)
//                findViewById<RecyclerView>(R.id.client_add_item_recyclerView).setHasFixedSize(true)
            }

        return list


    }
}