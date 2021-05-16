package com.example.deliverinator.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverinator.R
import kotlinx.android.synthetic.main.admin_fragment_delete.*

class DeleteFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.admin_fragment_delete, container, false)
        val list = generateList()

        recyclerView = view.findViewById(R.id.admin_delete_recycler_view)
        recyclerView.adapter = DeleteFragmentRecyclerAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize((true))
        // Inflate the layout for this fragment
        return view
    }

    private fun generateList():List<DeleteFragmentRecyclerItem> {

        val list = ArrayList<DeleteFragmentRecyclerItem>()
        val item = DeleteFragmentRecyclerItem(R.drawable.sobolan, "Sobolan", "Dai 10 lei vomiti cat vrei")
        list += item

        return list
    }
}