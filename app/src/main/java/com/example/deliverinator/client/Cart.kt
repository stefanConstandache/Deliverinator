package com.example.deliverinator.client

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.CART_ITEMS
import com.example.deliverinator.R
import com.example.deliverinator.UploadMenuItem
import kotlinx.android.synthetic.main.activity_cart.*

class Cart : AppCompatActivity(), CartItemAdapter.OnItemClickListener {
    private lateinit var mItemsList: ArrayList<Pair<UploadMenuItem, Int>>
    private lateinit var mCartItemAdapter: CartItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val items =
            intent.getBundleExtra(CART_ITEMS)!!.get(CART_ITEMS) as HashMap<UploadMenuItem, Int>

        mItemsList = getItemsList(items)
        mCartItemAdapter = CartItemAdapter(this, mItemsList, this)

        cart_items_recyclerView.apply {
            adapter = mCartItemAdapter
            layoutManager = LinearLayoutManager(this@Cart)
            setHasFixedSize(true)
        }
    }

    private fun getItemsList(items: HashMap<UploadMenuItem, Int>): ArrayList<Pair<UploadMenuItem, Int>> {
        val list = ArrayList<Pair<UploadMenuItem, Int>>()

        for (item in items) {
            list.add(Pair(item.key, item.value))
        }

        return list
    }

    override fun onItemDeleteClick(position: Int) {
        // Nu mere
        mItemsList.removeAt(position)
    }

    override fun onItemAddClick(position: Int, textView: TextView) {
        val quantity = textView.text.toString().toInt() + 1

        textView.text = "$quantity"
        mItemsList[position] = mItemsList[position].copy(second = quantity)
    }

    override fun onItemRemoveClick(position: Int, textView: TextView) {
        if (textView.text.toString().toInt() > 1) {
            val quantity = textView.text.toString().toInt() - 1

            textView.text = "$quantity"
            mItemsList[position] = mItemsList[position].copy(second = quantity)
        }
    }

    override fun onBackPressed() {

    }
}