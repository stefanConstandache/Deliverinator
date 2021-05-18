package com.example.deliverinator.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.example.deliverinator.restaurant.MenuAdapter
import kotlinx.android.synthetic.main.admin_delete_recycler_item.view.*
import kotlinx.android.synthetic.main.restaurant_menu_item.view.*

class DeleteFragmentRecyclerAdapter(
    private val list: List<DeleteFragmentRecyclerItem>,
    private val listener: DeleteFragmentRecyclerAdapter.OnItemClickListener
) : RecyclerView.Adapter<DeleteFragmentRecyclerAdapter.AdminDeleteViewHolder>() {

    inner class AdminDeleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val itemImage = itemView.admin_delete_restaurant_image_view
        val itemName = itemView.admin_delete_restaurant_text_big
        val itemDescription = itemView.admin_delete_restaurant_text_small

        init {
            itemView.admin_delete_restaurant_delete.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            val position = absoluteAdapterPosition

            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDeleteViewHolder {


        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.admin_delete_recycler_item, parent, false)

        return AdminDeleteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminDeleteViewHolder, position: Int) {

        val currentItem = list[position]

        holder.itemImage.setImageResource(currentItem.imageResource)
        holder.itemName.text = currentItem.text1
        holder.itemDescription.text = currentItem.text2
    }

    override fun getItemCount() = list.size

    class DeleteFragmentRecyclerViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.admin_delete_restaurant_image_view)
        val textView1:TextView = itemView.findViewById(R.id.admin_delete_restaurant_text_big)
        val textView2:TextView = itemView.findViewById(R.id.admin_delete_restaurant_text_small)
    }
}