package com.example.deliverinator.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.example.deliverinator.R
import com.example.deliverinator.UploadMenuItem
import com.example.deliverinator.Utils.Companion.format
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_menu_item.view.*
import kotlin.math.round

class MenuAdapter(
    private val context: Context,
    private val uploadsList: List<UploadMenuItem>,
    private val listener: OnItemClickListener
) : Adapter<MenuAdapter.MenuViewHolder>() {
    inner class MenuViewHolder(itemView: View) : ViewHolder(itemView) {
        val itemImage = itemView.restaurant_menu_item_image
        val itemName = itemView.restaurant_menu_item_name
        val itemDescription = itemView.restaurant_menu_item_description
        val itemPrice = itemView.restaurant_menu_item_price
        val isAvailable = itemView.restaurant_menu_checkBox
        val moreActions = itemView.restaurant_menu_more
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
        fun onCheckBoxClick(position: Int, state: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.restaurant_menu_item, parent, false)

        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val uploadCurrent = uploadsList[position]

        holder.itemName.text = uploadCurrent.itemName
        holder.itemDescription.text = uploadCurrent.itemDescription
        holder.itemPrice.text = StringBuilder().append(uploadCurrent.itemPrice.format(2)).append(" RON")
        holder.isAvailable.isChecked = uploadCurrent.isAvailable

        holder.moreActions.setOnClickListener {
            listener.onItemClick(position, it)
        }

        holder.isAvailable.setOnCheckedChangeListener { _, state ->
            listener.onCheckBoxClick(position, state)
        }

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