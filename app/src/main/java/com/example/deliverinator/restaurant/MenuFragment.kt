package com.example.deliverinator.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import kotlinx.android.synthetic.main.restaurant_fragment_menu.*

class MenuFragment : Fragment() {
    private lateinit var restaurantRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.restaurant_fragment_menu, container, false)
        val menuItems = generateDummyList()

        restaurantRecyclerView = view.findViewById(R.id.restaurant_fragment_recyclerView)

        restaurantRecyclerView.adapter = MenuAdapter(menuItems)
        restaurantRecyclerView.layoutManager = LinearLayoutManager(context)
        restaurantRecyclerView.setHasFixedSize(true)

        return view
    }

    private fun generateDummyList(): List<MenuItem> {
        val list = ArrayList<MenuItem>()

        val burger = MenuItem(R.drawable.burger, "Burger", "Cel mai dulce si frumos burger", true)
        val shaorma = MenuItem(R.drawable.shaorma, "Shaorma", "Shaorma cu de toate pentru fetele tunate", true)
        val pizza = MenuItem(R.drawable.pizza, "Pizza", "Pizza ca a lu' Tanti Mitza", true)

        list += burger
        list += shaorma
        list += pizza

        return list
    }
}