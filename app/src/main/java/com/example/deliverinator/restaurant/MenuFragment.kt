package com.example.deliverinator.restaurant

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.IMAGE_PICK_CODE
import com.example.deliverinator.PERMISSION_CODE
import com.example.deliverinator.R
import kotlinx.android.synthetic.main.restaurant_add_item_dialog.*
import kotlinx.android.synthetic.main.restaurant_add_item_dialog.view.*
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.*

class MenuFragment : Fragment(), MenuAdapter.OnItemClickListener {
    private lateinit var mAdapter: MenuAdapter
    private lateinit var mMenuItems: ArrayList<MenuItem>
    private lateinit var mDialogImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.restaurant_fragment_menu, container, false)

        mMenuItems = generateDummyList()
        mAdapter = MenuAdapter(mMenuItems, this)

        view.restaurant_fragment_recyclerView.adapter = mAdapter
        view.restaurant_fragment_recyclerView.layoutManager = LinearLayoutManager(context)
        view.restaurant_fragment_recyclerView.setHasFixedSize(true)

        view.restaurant_fragment_fab.setOnClickListener {
            addMenuItem(view)
        }

        return view
    }

    private fun addMenuItem(view: View) {
        val builder = AlertDialog.Builder(view.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.restaurant_add_item_dialog, null)

        mDialogImageView = dialogLayout.restaurant_add_dialog_imageView

        dialogLayout.restaurant_add_button.setOnClickListener {
            chooseImage()
        }

        builder.setTitle(R.string.add_item)
            .setPositiveButton(R.string.add) { _, _ ->
                val newItem = MenuItem(
                    R.drawable.pizza, // De schimbat
                    dialogLayout.restaurant_add_name.text.toString(),
                    dialogLayout.restaurant_add_description.text.toString(),
                    true
                )

                mMenuItems.add(newItem)
                mAdapter.notifyItemInserted(mMenuItems.size - 1)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
            }

        val dialog = builder.create()

        dialog.setView(dialogLayout)
        dialog.show()

        dialog.setOnShowListener {
        }
    }

    private fun generateDummyList(): ArrayList<MenuItem> {
        val list = ArrayList<MenuItem>()

        val burger = MenuItem(R.drawable.burger, "Burger", "Cel mai dulce si frumos burger", true)
        val shaorma = MenuItem(R.drawable.shaorma, "Shaorma", "Shaorma cu de toate pentru fetele tunate", true)
        val pizza = MenuItem(R.drawable.pizza, "Pizza", "Pizza ca a lu' Tanti Mitza", true)

        list += burger
        list += shaorma
        list += pizza

        return list
    }

    override fun onItemClick(position: Int) {
        mMenuItems.removeAt(position)
        mAdapter.notifyItemRemoved(position)
    }

    private fun chooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = context?.let { context ->
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (permission == PackageManager.PERMISSION_DENIED) {
                // Request permission
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                // Show pop-up
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                chooseImageFromGallery()
            }
        } else {
            chooseImageFromGallery()
        }
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageFromGallery()
                } else {
                    Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mDialogImageView.setImageURI(data?.data)
        }
    }
}