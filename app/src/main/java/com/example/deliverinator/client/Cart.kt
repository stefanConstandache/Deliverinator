package com.example.deliverinator.client

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.*
import com.example.deliverinator.Utils.Companion.format
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.client_cart_item.*

class Cart : AppCompatActivity(), CartItemAdapter.OnItemClickListener {
    private lateinit var mItemsList: ArrayList<Pair<UploadMenuItem, Int>>
    private lateinit var mCartItemAdapter: CartItemAdapter
    private lateinit var mDocReference: DocumentReference
    private lateinit var mEmail: String
    private lateinit var mDatabaseRef: DatabaseReference
    private var mCartItemsSum: Double = 0.0
    private var mChannelId = "channel_id"
    private val mNotificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val items = intent.getBundleExtra(CART_ITEMS)!!.get(CART_ITEMS) as HashMap<UploadMenuItem, Int>

        mDocReference = FirebaseFirestore.getInstance()
            .collection(USERS)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
        mItemsList = getItemsList(items)
        mCartItemAdapter = CartItemAdapter(this, mItemsList, this)
        mEmail = intent.getStringExtra(EMAIL)!!
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mEmail).child(ORDERS)
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
        mCartItemsSum -= mItemsList[position].first.itemPrice * mItemsList[position].second
        mItemsList.removeAt(position)
        mCartItemAdapter.notifyDataSetChanged()

        if (mItemsList.isNotEmpty()) {
            setFABText(mCartItemsSum)
        } else {
            onBackPressed()
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

    override fun onBackPressed() {
        val data = Intent(this, ClientRestaurantMenu::class.java)
        val items = HashMap<UploadMenuItem, Int>()

        if (mItemsList.isNotEmpty()) {
            for (item in mItemsList) {
                items[item.first] = item.second
            }
        }

        val bundle = bundleOf(MENU_ITEMS to items)

        data.putExtra(MENU_ITEMS, bundle)

        setResult(1, data)
        finish()
    }

    fun launchAddressDialog(view: View) {
        val addressField = EditText(view.context)
        val addressDialog = AlertDialog.Builder(view.context)

        mDocReference.get().addOnSuccessListener { docSnapshot ->
            val address = docSnapshot.getString(ADDRESS)
            val message = if (address != null && address.isNotEmpty()) {
                addressField.setText(address)
                getString(R.string.confirm_adress)
            } else {
                getString(R.string.provide_adress)
            }

            val dialog = addressDialog.setTitle("Enter your address")
                .setMessage(message)
                .setView(addressField)
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, null)
                .create()

            dialog.setOnShowListener {
                val confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                confirmButton.setOnClickListener {
                    val fieldText = addressField.text.toString().trim()

                    if (fieldText.isEmpty()) {
                        addressField.error = getString(R.string.empty_field)
                        return@setOnClickListener
                    }

                    mDocReference.set(
                        hashMapOf(ADDRESS to fieldText),
                        SetOptions.merge()
                    )

                    val order = getOrderString(mItemsList)
                    val upload = UploadOrder(
                        docSnapshot.getString(NAME),
                        fieldText,
                        order,
                        mCartItemsSum
                    )

                    val uploadId = mDatabaseRef.push().key

                    if (uploadId != null) {
                        mDatabaseRef.child(uploadId).setValue(upload)
                    }

                    dialog.dismiss()
                    // TODO: Inchis activitati
                    createNotificationChannel()
                    sendNotification()
                }

                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
            }

            dialog.show()
        }
    }

    private fun getOrderString(itemsList: ArrayList<Pair<UploadMenuItem, Int>>): String {
        val builder = StringBuilder()

        for (index in 0 until itemsList.size) {
            val item = itemsList[index]

            builder.append(item.second).append(" x ").append(item.first.itemName)

            if (index != itemsList.size - 1) {
                builder.append(", ")
            }
        }

        return builder.toString()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(mChannelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.splash_image)
        val builder = NotificationCompat.Builder(this, mChannelId)
            .setSmallIcon(R.drawable.splash_image)
            .setContentTitle("Order sent!")
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
           // .setOngoing(true)

        NotificationManagerCompat.from(this).run {
            notify(mNotificationId, builder.build())
        }
    }
}