package com.example.deliverinator.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import com.example.deliverinator.R
import com.example.deliverinator.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.*
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.restaurant_fragment_recyclerView
import kotlinx.android.synthetic.main.restaurant_fragment_orders.view.*

class OrdersFragment : Fragment() {
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAdapter: OrdersItemAdapter
    private lateinit var mOrdersList: ArrayList<OrdersItem>
    private lateinit var mEmail: String

    companion object{
        private lateinit var mDBListener: ValueEventListener

        val DBListener
            get() = mDBListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.restaurant_fragment_orders, container, false)

        mAuth = FirebaseAuth.getInstance()
        mEmail = mAuth.currentUser?.email!!.replace("[@.]".toRegex(), "_")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mEmail).child(ORDERS)
        mOrdersList = ArrayList()
        mAdapter = OrdersItemAdapter(context!!, mOrdersList)

        view.restaurant_fragment_orders_recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context!!)
            setHasFixedSize(true)
        }

        mDBListener = mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mOrdersList.clear()

                for (postSnapshot in snapshot.children) {
                    val upload = postSnapshot.getValue(OrdersItem::class.java)

                    if (upload != null) {
                        upload.key = postSnapshot.key
                        mOrdersList.add(upload)
                    }
                }

                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}