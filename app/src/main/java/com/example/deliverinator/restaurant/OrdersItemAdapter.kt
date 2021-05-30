package com.example.deliverinator.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.deliverinator.R
import com.example.deliverinator.Utils.Companion.format
import com.example.deliverinator.client.CartItemAdapter
import kotlinx.android.synthetic.main.admin_delete_recycler_item.view.*
import kotlinx.android.synthetic.main.restaurant_fragment_orders_item.view.*
import java.lang.StringBuilder

class OrdersItemAdapter (
    private val context: Context,
    private val ordersList: List<OrdersItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<OrdersItemAdapter.OrdersItemViewHolder>(){
    inner class OrdersItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val itemName = itemView.restaurant_orders_name
        val itemAddress = itemView.restaurant_orders_address
        val itemOrder = itemView.restaurant_orders_order
        val itemPrice = itemView.restaurant_orders_price

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = absoluteAdapterPosition

            if (position != NO_POSITION) {
                listener.onItemClick(position,view)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
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