package com.example.deliverinator.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R

class DeleteFragmentRecyclerAdapter(private val list: List<DeleteFragmentRecyclerItem>) :
    RecyclerView.Adapter<DeleteFragmentRecyclerAdapter.DeleteFragmentRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeleteFragmentRecyclerViewHolder {

        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.admin_delete_recycler_item, parent, false)

        return DeleteFragmentRecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeleteFragmentRecyclerViewHolder, position: Int) {

        val currentItem = list[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2
    }

    override fun getItemCount() = list.size

    class DeleteFragmentRecyclerViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.admin_delete_restaurant_image_view)
        val textView1:TextView = itemView.findViewById(R.id.admin_delete_restaurant_text_big)
        val textView2:TextView = itemView.findViewById(R.id.admin_delete_restaurant_text_small)
    }
}