package com.example.deliverinator.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.*
import com.example.deliverinator.restaurant.MenuAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.admin_fragment_delete.view.*
import kotlinx.android.synthetic.main.client_fragment_restaurants.view.*

class RestaurantsFragment : Fragment(), RestaurantItemAdapter.OnItemClickListener {
    var database = FirebaseFirestore.getInstance()
    private lateinit var mRestaurantsFragment: RestaurantsFragment
    lateinit var adapter: RestaurantItemAdapter
    lateinit var restaurantsList:ArrayList<RestaurantItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.client_fragment_restaurants, container, false)

        restaurantsList = generateDummyList(view)

        view.findViewById<RecyclerView>(R.id.client_restaurants_recycler_view).adapter = RestaurantItemAdapter(
            context!!,
            restaurantsList,
            this
        )
        view.findViewById<RecyclerView>(R.id.client_restaurants_recycler_view).layoutManager = LinearLayoutManager(
            context
        )

        return view
    }

    private fun generateDummyList(view: View): ArrayList<RestaurantItem> {
        val list = ArrayList<RestaurantItem>()

        database.collection(RESTAURANTS).get()
            .addOnSuccessListener{ documents ->
                for(document in documents) {
                    val item = RestaurantItem(
                        document.getString(RESTAURANT_IMAGE), document.getString(
                            NAME
                        )!!,
                        document.getString(RESTAURANT_DESCRIPTION)!!
                    )
                    list.add(item)
                }
                adapter = RestaurantItemAdapter(context!!, list,this)

                view.client_restaurants_recycler_view.adapter = adapter
                view.client_restaurants_recycler_view.layoutManager = LinearLayoutManager(context)
                view.client_restaurants_recycler_view.setHasFixedSize(true)
            }

        return list
    }

    override fun onItemClick(position: Int, view: View?) {
        val restaurantName = restaurantsList[position]
        val intent = Intent(context, ActivityClientAddItemsFromMenu::class.java)
        intent.putExtra(NAME,restaurantName.name)
        startActivity(intent)

    }

}