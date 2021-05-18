package com.example.deliverinator.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import kotlinx.android.synthetic.main.admin_fragment_delete.*
import kotlinx.android.synthetic.main.admin_fragment_delete.view.*
import kotlinx.android.synthetic.main.restaurant_fragment_menu.view.*

class DeleteFragment : Fragment(),DeleteFragmentRecyclerAdapter.OnItemClickListener {
    lateinit var adapter: DeleteFragmentRecyclerAdapter
    lateinit var list:ArrayList<DeleteFragmentRecyclerItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.admin_fragment_delete, container, false)
        list = generateList()
        adapter = DeleteFragmentRecyclerAdapter(list,this)

        view.admin_delete_recycler_view.adapter = adapter
        view.admin_delete_recycler_view.layoutManager = LinearLayoutManager(context)
        view.admin_delete_recycler_view.setHasFixedSize(true)

        // Inflate the layout for this fragment
        return view
    }

    private fun generateList():ArrayList<DeleteFragmentRecyclerItem> {

        val list = ArrayList<DeleteFragmentRecyclerItem>()
        val item = DeleteFragmentRecyclerItem(R.drawable.sobolan, "Sobolan", "Dai 10 lei vomiti cat vrei")
        list += item

        return list
    }

    override fun onItemClick(position: Int) {
        list.removeAt(position)
        adapter.notifyItemRemoved(position)
    }
}