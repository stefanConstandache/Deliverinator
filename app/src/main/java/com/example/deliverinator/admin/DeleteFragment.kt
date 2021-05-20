package com.example.deliverinator.admin

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverinator.*
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.admin_fragment_delete.view.*

class DeleteFragment : Fragment(),DeleteFragmentRecyclerAdapter.OnItemClickListener {
    lateinit var adapter: DeleteFragmentRecyclerAdapter
    lateinit var list:ArrayList<DeleteFragmentRecyclerItem>
    var database = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.admin_fragment_delete, container, false)
        list = generateList(view)

        return view
    }

    private fun generateList(view: View):ArrayList<DeleteFragmentRecyclerItem> {

        list = ArrayList<DeleteFragmentRecyclerItem>()
        database.collection(RESTAURANTS).get()
            .addOnSuccessListener{ documents ->
                for(document in documents) {
                    val item = DeleteFragmentRecyclerItem(
                        R.drawable.sobolan, document.getString(
                            NAME
                        )!!,
                        document.getString(RESTAURANT_DESCRIPTION)!!
                    )
                    list.add(item)
                }
                adapter = DeleteFragmentRecyclerAdapter(list, this)

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
}