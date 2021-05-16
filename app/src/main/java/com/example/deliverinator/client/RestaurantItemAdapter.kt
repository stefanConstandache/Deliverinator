package com.example.deliverinator.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R

class RestaurantItemAdapter(private val exampleList: List<RestaurantItem>) : RecyclerView.Adapter<RestaurantItemAdapter.RestaurantItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.client_restaurants_item,
            parent, false)

        return RestaurantItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantItemViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
    }

    override fun getItemCount() = exampleList.size

    class RestaurantItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.restaurant_image_view)
        val name: TextView = itemView.findViewById(R.id.restaurant_name)
        val description: TextView = itemView.findViewById(R.id.restaurant_description)
    }
}