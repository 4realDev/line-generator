package com.example.furnitures.calculator.bottombar.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.selection.ListTrickAdapter
import com.example.furnitures.calculator.trick.FurnitureViewState

class ListTrickFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ListTrickContract.ViewModel

    private lateinit var listTrickItemMoveCallback: ListTrickItemMoveCallback
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val adapter = ListTrickAdapter(
        object : ListTrickAdapter.FurnitureModificationListener {
            override fun onItemRemove(furnitureItem: FurnitureViewState) {
                viewModel.removeFurnitureItem(furnitureItem)
            }

            override fun onItemMove(fromPosition: Int, toPosition: Int) {
                viewModel.changeFurnitureItemPosition(fromPosition, toPosition)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(ListTrickViewModel::class.java)
        listTrickItemMoveCallback = ListTrickItemMoveCallback(adapter)
        itemTouchHelper = ItemTouchHelper(listTrickItemMoveCallback)

        // Use ItemTouchHelper (convenience class / bequemlichkeits Klasse)
        // Class that make our RecyclerView swipable
        // Pass SimpleCallBack to create the two Methods
        // SimpleCallback wants two parameters -> drag and drop direction and swipe direction
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_select_tricks, container, false)
        recyclerView = view.findViewById(R.id.fragment_select_tricks__recycler)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observed alle veränderungen der Liste und gibt sie an den Adapter weiter
        // submitList löst automatisch notifyDataSetChanged aus
        viewModel.getSelectedItemsViewState().observe(viewLifecycleOwner, Observer{ newData
            -> if (newData != null) adapter.onFurnitureItemsUpdate(newData)
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    companion object {
        fun newInstance(): ListTrickFragment {
            return ListTrickFragment()
        }
    }


}
