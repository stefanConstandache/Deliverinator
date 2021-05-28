package com.example.deliverinator.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.example.deliverinator.UploadMenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_restaurant_menu_item.view.*
import java.lang.StringBuilder

class ClientRestaurantMenuItemAdapter(
    private val context: Context,
    private val itemsList: ArrayList<UploadMenuItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClientRestaurantMenuItemAdapter.ClientAddItemViewHolder>() {
    inner class ClientAddItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage = itemView.client_menu_item_image_view
        val itemName = itemView.client_menu_item_name
        val itemDescription = itemView.client_menu_item_description
        val itemPrice = itemView.client_menu_item_price
        val itemAddToCart = itemView.client_menu_item_add_to_cart
    }

    interface OnItemClickListener {
        fun onAddToCartClick(position: Int, view: View?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientRestaurantMenuItemAdapter.ClientAddItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_restaurant_menu_item,
            parent,
            false
        )

        return ClientAddItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ClientRestaurantMenuItemAdapter.ClientAddItemViewHolder,
        position: Int
    ) {
        val currentItem = itemsList[position]

        holder.itemName.text = currentItem.itemName
        holder.itemDescription.text = currentItem.itemDescription
        holder.itemPrice.text = StringBuilder().append(currentItem.itemPrice).append(" RON")

        holder.itemAddToCart.setOnClickListener {
            listener.onAddToCartClick(position, it)
        }

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

    override fun getItemCount() = itemsList.size
}