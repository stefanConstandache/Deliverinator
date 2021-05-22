package com.example.deliverinator.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.admin_delete_recycler_item.view.*

class DeleteFragmentRecyclerAdapter(
    private val context: Context,
    private val listener: DeleteFragmentRecyclerAdapter.OnItemClickListener,
    private val uploadsList: List<DeleteFragmentRecyclerItem>,
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
                listener.onItemClick(position,view)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View?)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDeleteViewHolder {


        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.admin_delete_recycler_item, parent, false)

        return AdminDeleteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminDeleteViewHolder, position: Int) {
        val uploadCurrent = uploadsList[position]
        //val currentItem = list[position]

       // holder.itemImage.setImageResource(currentItem.imageResource)
        holder.itemName.text = uploadCurrent.text1
        holder.itemDescription.text = uploadCurrent.text2

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

    override fun getItemCount() = uploadsList.size

}