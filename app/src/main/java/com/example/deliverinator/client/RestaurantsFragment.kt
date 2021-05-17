package com.example.deliverinator.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R

class RestaurantsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.client_fragment_restaurants, container, false)

        val restaurantsList = generateDummyList(10)

        view.findViewById<RecyclerView>(R.id.client_restaurants_recycler_view).adapter = RestaurantItemAdapter(restaurantsList)
        view.findViewById<RecyclerView>(R.id.client_restaurants_recycler_view).layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun generateDummyList(size: Int): List<RestaurantItem> {
        val list = ArrayList<RestaurantItem>()

        for (i in 0 until size) {
            val drawable = R.drawable.ic_restaurant
            val item = RestaurantItem(drawable, "Restaurant $i", "Descriere $i")
            list += item
        }

        return list
    }
}