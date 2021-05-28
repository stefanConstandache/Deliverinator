package com.example.deliverinator.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.example.deliverinator.UploadMenuItem
import kotlinx.android.synthetic.main.client_cart_item.view.*
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class CartItemAdapter(
    private val context: Context,
    private val cartList: List<Pair<UploadMenuItem, Int>>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {
        inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val itemImage = itemView.client_cart_item_image_view
            val itemName = itemView.client_cart_item_name
            val itemPrice = itemView.client_cart_item_price
            val itemAdd = itemView.client_cart_add
            val itemRemove = itemView.client_cart_remove
            val itemDelete = itemView.client_cart_delete
            val itemQuantity = itemView.client_cart_item_quantity
        }

    interface OnItemClickListener {
        fun onItemDeleteClick(position: Int)
        fun onItemAddClick(position: Int)
        fun onItemRemoveClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.client_cart_item, parent, false)

        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartList[position]

        holder.itemName.text = currentItem.first.itemName
        holder.itemPrice.text = StringBuilder().append(currentItem.first.itemPrice).append(" RON")
        holder.itemQuantity.text = currentItem.second.toString()

        holder.itemAdd.setOnClickListener {
            listener.onItemAddClick(position)
        }

        holder.itemRemove.setOnClickListener {
            listener.onItemRemoveClick(position)
        }

        holder.itemDelete.setOnClickListener {
            listener.onItemDeleteClick(position)
        }

        if(currentItem.first.imageUrl == null) {
            Picasso.with(context)
                .load(R.drawable.ic_food)
                .placeholder(R.drawable.ic_food)
                .fit()
                .centerCrop()
                .into(holder.itemImage)
        } else {
            Picasso.with(context)
                .load(currentItem.first.imageUrl)
                .placeholder(R.drawable.ic_food)
                .fit()
                .centerCrop()
                .into(holder.itemImage)
        }
    }

    override fun getItemCount(): Int = cartList.size
}