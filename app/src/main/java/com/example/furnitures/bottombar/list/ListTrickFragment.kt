package com.example.furnitures.bottombar.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.R
import com.example.furnitures.helper.ItemDecorationEqualSpacing
import com.example.furnitures.trick.FurnitureViewState

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

            // wird durch onStartDrag aufgerufen
            override fun onItemMove(fromIndex: Int, toIndex: Int) {
                viewModel.changeFurnitureItemPosition(fromIndex, toIndex)
            }
        },
        object : ListTrickAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListTrickViewModel::class.java)
        listTrickItemMoveCallback = ListTrickItemMoveCallback(adapter)
        itemTouchHelper = ItemTouchHelper(listTrickItemMoveCallback)

        requireActivity().title = "YOUR TRICK LIST"

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
        requireActivity().title = getString(R.string.toolbar_title_list)
        val spacing = resources.getDimensionPixelSize(com.example.furnitures.R.dimen.fragment_list__recycler_spacing)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val spaceLinear = ItemDecorationEqualSpacing(spacing)
        recyclerView.addItemDecoration(spaceLinear)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Observed alle veränderungen der Liste und gibt sie an den Adapter weiter
        // submitList löst automatisch notifyDataSetChanged aus
        viewModel.getSelectedItemsViewState().observe(viewLifecycleOwner, Observer { newData
            ->
            if (newData != null) adapter.onFurnitureItemsUpdate(newData)
        })

        loadAnimation()
    }

    private fun loadAnimation() {
        val animFallDownRecycler = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        animFallDownRecycler.animation.startOffset = 0
        animFallDownRecycler.animation.duration = 300
        animFallDownRecycler.delay = 0.25f
        recyclerView.layoutAnimation = animFallDownRecycler
        recyclerView.startLayoutAnimation()
    }

    companion object {
        fun newInstance(): ListTrickFragment = ListTrickFragment()
    }


}
