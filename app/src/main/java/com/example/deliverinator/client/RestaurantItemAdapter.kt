package com.example.deliverinator.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_restaurants_item.view.*
import androidx.recyclerview.widget.RecyclerView.*
import com.example.deliverinator.restaurant.MenuAdapter
import kotlinx.android.synthetic.main.client_fragment_restaurants.view.*
import kotlinx.android.synthetic.main.restaurant_menu_item.view.*

class RestaurantItemAdapter(
    private val context: Context,
    private val exampleList: List<RestaurantItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RestaurantItemAdapter.RestaurantItemViewHolder>() {
    inner class RestaurantItemViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val itemImage = itemView.restaurant_image_view
        val itemName = itemView.restaurant_name
        val itemDescription = itemView.restaurant_description

        init {
            itemView.restaurant_image_view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = absoluteAdapterPosition

            if (position != NO_POSITION) {
                listener.onItemClick(position, view)
            }
        }

    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.client_restaurants_item,
            parent, false)

        return RestaurantItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantItemViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.itemName.text = currentItem.name
        holder.itemDescription.text = currentItem.description

        if (currentItem.imageUrl == null) {
            Picasso.with(context)
                .load(R.drawable.ic_food)
                .placeholder(R.drawable.ic_food)
                .fit()
                .centerCrop()
                .into(holder.itemImage)
        } else {
            Picasso.with(context)
                .load(currentItem.imageUrl)
                .placeholder(R.drawable.ic_food)
                .fit()
                .centerCrop()
                .into(holder.itemImage)
        }
    }

    override fun getItemCount() = exampleList.size
}