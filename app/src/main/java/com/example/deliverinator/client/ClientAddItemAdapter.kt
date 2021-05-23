package com.example.deliverinator.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.example.deliverinator.UploadMenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_add_items_from_menu_item.view.*

class ClientAddItemAdapter (
    private val context: Context,
    private val itemsList: ArrayList<UploadMenuItem>,
    private val listener: OnItemClickListener
):RecyclerView.Adapter<ClientAddItemAdapter.ClientAddItemViewHolder>(){
    inner class ClientAddItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage = itemView.client_add_item_image_view
        val itemName = itemView.client_add_item_name
        val itemDescription = itemView.client_add_item_description
        val itemQuantity = itemView.client_add_item_quantity
        val addView = itemView.client_add_item_add
        val removeView = itemView.client_add_item_remove
    }

    interface OnItemClickListener {
        fun onAddClick(position: Int, view: View?)
        fun onRemoveClick(position: Int, view: View?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientAddItemAdapter.ClientAddItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_add_items_from_menu_item,
            parent,
            false
        )

        return ClientAddItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ClientAddItemAdapter.ClientAddItemViewHolder,
        position: Int
    ) {
        val currentItem = itemsList[position]

        holder.itemName.text = currentItem.itemName
        holder.itemDescription.text = currentItem.itemDescription

        holder.addView.setOnClickListener {
            listener.onAddClick(position, holder.itemQuantity)
        }

        holder.removeView.setOnClickListener {
            listener.onRemoveClick(position, holder.itemQuantity)
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