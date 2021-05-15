package com.example.deliverinator.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.R
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.*

class MenuFragment : Fragment(), MenuAdapter.OnItemClickListener {
    private lateinit var mAdapter: MenuAdapter
    private lateinit var mMenuItems: ArrayList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.restaurant_fragment_menu, container, false)

        mMenuItems = generateDummyList()
        mAdapter = MenuAdapter(mMenuItems, this)

        view.restaurant_fragment_recyclerView.adapter = mAdapter
        view.restaurant_fragment_recyclerView.layoutManager = LinearLayoutManager(context)
        view.restaurant_fragment_recyclerView.setHasFixedSize(true)

        view.restaurant_fragment_fab.setOnClickListener {
            addMenuItem()
        }

        return view
    }

    private fun addMenuItem() {
        val shaorma = MenuItem(R.drawable.shaorma, "Shaorma", "Shaorma cu de toate pentru fetele tunate", true)

        mMenuItems.add(shaorma)
        mAdapter.notifyItemInserted(mMenuItems.size - 1)
    }

    private fun generateDummyList(): ArrayList<MenuItem> {
        val list = ArrayList<MenuItem>()

        val burger = MenuItem(R.drawable.burger, "Burger", "Cel mai dulce si frumos burger", true)
        val shaorma = MenuItem(R.drawable.shaorma, "Shaorma", "Shaorma cu de toate pentru fetele tunate", true)
        val pizza = MenuItem(R.drawable.pizza, "Pizza", "Pizza ca a lu' Tanti Mitza", true)

        list += burger
        list += shaorma
        list += pizza

        return list
    }

    override fun onItemClick(position: Int) {
        mMenuItems.removeAt(position)
        mAdapter.notifyItemRemoved(position)
    }
}