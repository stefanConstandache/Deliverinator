package com.example.deliverinator.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.deliverinator.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_menu_item.view.*

class MenuAdapter(
    private val context: Context,
    private val uploadsList: List<UploadMenuItem>,
    private val listener: OnItemClickListener
) : Adapter<MenuAdapter.MenuViewHolder>() {
    inner class MenuViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val itemImage = itemView.restaurant_menu_item_image
        val itemName = itemView.restaurant_menu_item_name
        val itemDescription = itemView.restaurant_menu_item_description
        val isAvailable = itemView.restaurant_menu_checkBox

        init {
            itemView.restaurant_menu_more.setOnClickListener(this)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.restaurant_menu_item, parent, false)

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val uploadCurrent = uploadsList[position]

        holder.itemName.text = uploadCurrent.itemName
        holder.itemDescription.text = uploadCurrent.itemDescription
        holder.isAvailable.isChecked = uploadCurrent.isAvailable

        if (uploadCurrent.imageUrl == null) {
            Picasso.with(context)
                .load(R.drawable.ic_food)
                .placeholder(R.drawable.ic_food)
                .fit()
                .centerCrop()
                .into(holder.itemImage)
        } else {
            Picasso.with(context)
                .load(uploadCurrent.imageUrl)
                .placeholder(R.drawable.ic_food)
                .fit()
                .centerCrop()
                .into(holder.itemImage)
        }
    }

    override fun getItemCount(): Int = uploadsList.size
}