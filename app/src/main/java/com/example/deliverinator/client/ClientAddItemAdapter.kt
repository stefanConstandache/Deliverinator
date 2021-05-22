package com.example.deliverinator.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.client_add_items_from_menu_item.view.*

class ClientAddItemAdapter (
    private val context: Context,
    private val itemsList: ArrayList<ClientAddItemItem>,
   // private val listener: OnItemClickListener
):RecyclerView.Adapter<ClientAddItemAdapter.ClientAddItemViewHolder>(){
    inner class ClientAddItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val itemImage = itemView.client_add_item_image_view
        val itemName = itemView.client_add_item_item_name
        val itemDescription = itemView.client_add_item_item_description

        init {

        }
        override fun onClick(p0: View?) {
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientAddItemAdapter.ClientAddItemViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_add_items_from_menu_item,
            parent, false)

        return ClientAddItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ClientAddItemAdapter.ClientAddItemViewHolder,
        position: Int
    ) {
        val currentItem = itemsList[position]

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

    override fun getItemCount() = itemsList.size


}