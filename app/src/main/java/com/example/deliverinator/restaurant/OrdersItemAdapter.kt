package com.example.deliverinator.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.example.deliverinator.Utils.Companion.format
import kotlinx.android.synthetic.main.restaurant_fragment_orders_item.view.*
import java.lang.StringBuilder

class OrdersItemAdapter (
    private val context: Context,
    private val ordersList: List<OrdersItem>
) : RecyclerView.Adapter<OrdersItemAdapter.OrdersItemViewHolder>(){
    inner class OrdersItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName = itemView.restaurant_orders_name
        val itemAddress = itemView.restaurant_orders_address
        val itemOrder = itemView.restaurant_orders_order
        val itemPrice = itemView.restaurant_orders_price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersItemAdapter.OrdersItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.restaurant_fragment_orders_item, parent, false)

        return OrdersItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrdersItemViewHolder, position: Int) {
        val currentItem = ordersList[position]

        holder.itemName.text = currentItem.clientName
        holder.itemAddress.text = currentItem.address
        holder.itemOrder.text = currentItem.order
        holder.itemPrice.text = StringBuilder().append(currentItem.price.format(2)).append(" RON")
    }

    override fun getItemCount() = ordersList.size
}