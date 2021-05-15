package com.example.deliverinator.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import kotlinx.android.synthetic.main.restaurant_menu_item.view.*

class MenuAdapter(private val itemsList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage = itemView.restaurant_menu_item_image
        val itemName = itemView.restaurant_menu_item_name
        val itemDescription = itemView.restaurant_menu_item_description
        val isAvailable = itemView.restaurant_menu_checkBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_menu_item,
            parent, false)

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = itemsList[position]

        holder.itemImage.setImageResource(currentItem.imageResource)
        holder.itemName.text = currentItem.itemName
        holder.itemDescription.text = currentItem.itemDescription
        holder.isAvailable.isChecked = currentItem.isAvailable
    }

    override fun getItemCount(): Int = itemsList.size
}