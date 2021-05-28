package com.example.deliverinator.client

import android.os.Bundle
import android.widget.Toast
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

        val items = intent.getBundleExtra(CART_ITEMS)!!.get(CART_ITEMS) as HashMap<UploadMenuItem, Int>

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
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }

    override fun onItemAddClick(position: Int) {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }

    override fun onItemRemoveClick(position: Int) {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }
}