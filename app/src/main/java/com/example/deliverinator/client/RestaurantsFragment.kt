package com.example.deliverinator.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.client_fragment_restaurants.view.*

class RestaurantsFragment : Fragment(), RestaurantItemAdapter.OnItemClickListener {
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAdapter: RestaurantItemAdapter
    private lateinit var mRestaurantsList: ArrayList<RestaurantItem>
    private lateinit var mRestaurantsEmailList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.client_fragment_restaurants, container, false)

        mDatabase = FirebaseFirestore.getInstance()
        mRestaurantsList = getRestaurantsList()
        mRestaurantsEmailList = ArrayList()

        mDatabase.collection(RESTAURANTS)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    mRestaurantsEmailList.add(document.getString(EMAIL)!!)
                }
            }

        mAdapter = RestaurantItemAdapter(context!!, mRestaurantsList, this)

        view.client_restaurants_recycler_view.adapter = mAdapter
        view.client_restaurants_recycler_view.layoutManager = LinearLayoutManager(context)
        view.client_restaurants_recycler_view.setHasFixedSize(true)

        return view
    }

    private fun getRestaurantsEmails(): ArrayList<String> {
        val list = ArrayList<String>()

        mDatabase.collection(RESTAURANTS)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    list.add(document.getString(EMAIL)!!)
                }
            }

        return list
    }

    private fun getRestaurantsList(): ArrayList<RestaurantItem> {
        val list = ArrayList<RestaurantItem>()

        mDatabase.collection(RESTAURANTS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = RestaurantItem(
                        document.getString(RESTAURANT_IMAGE),
                        document.getString(NAME)!!,
                        document.getString(RESTAURANT_DESCRIPTION)!!
                    )

                    list.add(item)
                }

                mAdapter.notifyDataSetChanged()
            }

        return list
    }

    override fun onItemClick(position: Int, view: View?) {
        val restaurantEmail = mRestaurantsEmailList[position].replace("[@.]".toRegex(), "_")
        val intent = Intent(context, ActivityClientAddItemsFromMenu::class.java)

        intent.putExtra(EMAIL, restaurantEmail)
        startActivity(intent)
    }
}