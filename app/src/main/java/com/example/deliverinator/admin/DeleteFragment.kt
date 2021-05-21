package com.example.deliverinator.admin

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.*
import com.example.deliverinator.R
import com.example.deliverinator.restaurant.MenuAdapter
import com.example.deliverinator.restaurant.UploadMenuItem
import com.example.deliverinator.restaurant.defaultImageUri
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.admin_fragment_delete.view.*
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.*

class DeleteFragment : Fragment(),DeleteFragmentRecyclerAdapter.OnItemClickListener {
    lateinit var adapter: DeleteFragmentRecyclerAdapter
    lateinit var list:ArrayList<DeleteFragmentRecyclerItem>
    var database = FirebaseFirestore.getInstance()
    var mAuth = FirebaseAuth.getInstance()
    private lateinit var mStorageRef: StorageReference
    private lateinit var mDatabaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.admin_fragment_delete, container, false)

        mAuth = FirebaseAuth.getInstance()
        list = generateList(view)
        mStorageRef = FirebaseStorage.getInstance().getReference(mAuth.currentUser?.uid!!)
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mAuth.currentUser?.uid!!)


        return view
    }

    private fun generateList(view: View):ArrayList<DeleteFragmentRecyclerItem> {

        list = ArrayList<DeleteFragmentRecyclerItem>()
        database.collection(RESTAURANTS).get()
            .addOnSuccessListener{ documents ->
                for(document in documents) {
                    val item = DeleteFragmentRecyclerItem(
                        document.getString(RESTAURANT_IMAGE), document.getString(
                            NAME
                        )!!,
                        document.getString(EMAIL)!!
                    )
                    list.add(item)
                }
                adapter = DeleteFragmentRecyclerAdapter(context!!, this, list)

                view.admin_delete_recycler_view.adapter = adapter
                view.admin_delete_recycler_view.layoutManager = LinearLayoutManager(context)
                view.admin_delete_recycler_view.setHasFixedSize(true)
            }

        return list
    }

    override fun onItemClick(position: Int, view:View?) {
        val deleteDialog = AlertDialog.Builder(view!!.context)
        val restaurantName = list[position]

        deleteDialog
            .setTitle("Are you sure?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                database.collection(RESTAURANTS).whereEqualTo(NAME, restaurantName.text1)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            database.collection(RESTAURANTS).document(document.id).delete()
                            list.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        }
                    }

                database.collection(USERS).whereEqualTo(NAME, restaurantName.text1)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            database.collection(USERS).document(document.id).delete()

//                            val options = FirebaseOptions.builder()
//                                .setCredentials(GoogleCredentials.getApplicationDefault())
//                                .build()
//
//                            FirebaseApp.initializeApp(options)
//                            FirebaseAuth.getInstance().deleteUser(document.id);

                        }
                    }

            }
            .create()
            .show()
    }


    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = context?.contentResolver
        val mime = MimeTypeMap.getSingleton()

        return mime.getExtensionFromMimeType(contentResolver?.getType(uri))
    }
}