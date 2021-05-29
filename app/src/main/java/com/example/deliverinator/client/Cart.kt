package com.example.deliverinator.client

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.*
import com.example.deliverinator.Utils.Companion.format
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_cart.*

class Cart : AppCompatActivity(), CartItemAdapter.OnItemClickListener {
    private lateinit var mItemsList: ArrayList<Pair<UploadMenuItem, Int>>
    private lateinit var mCartItemAdapter: CartItemAdapter
    private lateinit var mDocReference: DocumentReference
    private var mCartItemsSum: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val items = intent.getBundleExtra(CART_ITEMS)!!.get(CART_ITEMS) as HashMap<UploadMenuItem, Int>

        mDocReference = FirebaseFirestore.getInstance()
            .collection(USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
        mItemsList = getItemsList(items)
        mCartItemAdapter = CartItemAdapter(this, mItemsList, this)
        mCartItemsSum = mItemsList.sumByDouble {
            it.first.itemPrice * it.second
        }

        setFABText(mCartItemsSum)

        cart_recyclerView.apply {
            adapter = mCartItemAdapter
            layoutManager = LinearLayoutManager(this@Cart)
            setHasFixedSize(true)
        }
    }

    private fun setFABText(cartItemsSum: Double) {
        cart_fab.text = StringBuilder("Order for ").append(cartItemsSum.format(2)).append(" RON")
    }

    private fun getItemsList(items: HashMap<UploadMenuItem, Int>): ArrayList<Pair<UploadMenuItem, Int>> {
        val list = ArrayList<Pair<UploadMenuItem, Int>>()

        for (item in items) {
            list.add(Pair(item.key, item.value))
        }

        return list
    }

    override fun onItemDeleteClick(position: Int) {
        mItemsList.removeAt(position)
        mCartItemAdapter.notifyDataSetChanged()

        if (mItemsList.isNotEmpty()) {
            mCartItemsSum -= mItemsList[position].first.itemPrice * mItemsList[position].second
            setFABText(mCartItemsSum)
        } else {
            finish()
        }
    }

    override fun onItemAddClick(position: Int, textView: TextView) {
        val quantity = textView.text.toString().toInt() + 1

        textView.text = "$quantity"
        mItemsList[position] = mItemsList[position].copy(second = quantity)

        mCartItemsSum += mItemsList[position].first.itemPrice
        setFABText(mCartItemsSum)
    }

    override fun onItemRemoveClick(position: Int, textView: TextView) {
        if (textView.text.toString().toInt() > 1) {
            val quantity = textView.text.toString().toInt() - 1

            textView.text = "$quantity"
            mItemsList[position] = mItemsList[position].copy(second = quantity)

            mCartItemsSum -= mItemsList[position].first.itemPrice
            setFABText(mCartItemsSum)
        }
    }

    fun launchAddressDialog(view: View) {
        val addressField = EditText(view.context)
        val addressDialog = AlertDialog.Builder(view.context)

        mDocReference.get().addOnSuccessListener {
            val address = it.getString(ADDRESS)
            val message = if (address != null && address.isNotEmpty()) {
                addressField.setText(address)
                "Please confirm your address"
            } else {
                "Please provide an address"
            }

            val dialog = addressDialog.setTitle("Enter your address")
                .setMessage(message)
                .setView(addressField)
                .setPositiveButton("Confirm", null)
                .setNegativeButton(R.string.cancel, null)
                .create()

            dialog.setOnShowListener {
                val confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                confirmButton.setOnClickListener {
                    val fieldText = addressField.text.toString().trim()

                    if (fieldText.isEmpty()) {
                        addressField.error = "Field cannot be empty"
                        return@setOnClickListener
                    }

                    mDocReference.set(
                        hashMapOf(ADDRESS to fieldText),
                        SetOptions.merge()
                    )

                    dialog.dismiss()
                    Toast.makeText(view.context, "TODO Notification sent!", Toast.LENGTH_SHORT).show()
                }

                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
            }

            dialog.show()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }
}