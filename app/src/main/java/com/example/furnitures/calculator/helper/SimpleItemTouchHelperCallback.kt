package com.example.furnitures.calculator.helper

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.example.furnitures.calculator.container.list.ListTrickContract
import com.example.furnitures.calculator.container.list.ListTrickFragment

class SimpleItemTouchHelperCallback(fragment: ListTrickFragment, viewModel: ListTrickContract.ViewModel) : ItemTouchHelper.Callback() {

    private var dragFrom = -1
    private var dragTo = -1
    private val listTrickFragment = fragment
    private val listTrickViewModel = viewModel

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        var fromPosition = viewHolder.adapterPosition
        var toPosition = target.adapterPosition

        // Verändere die dragFrom Position nur einmalig beim ersten Click
//        if(dragFrom == -1){
//            dragFrom = fromPosition
//        }
//
//        dragTo = toPosition

        listTrickFragment.onItemMoved(fromPosition, toPosition)

        Log.d("DEBUGG", "toPosition: $toPosition fromPosition: $fromPosition")

        recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var currentFurniture = listTrickFragment.getFurnitureAt(viewHolder.adapterPosition)
        listTrickFragment.onItemDelete(currentFurniture)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

//    private fun reallyMoved(from: Int, to: Int) {
//        viewModel.onItemMove(from, to)
//    }
//
//    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        super.clearView(recyclerView, viewHolder)
//
//        if (dragFrom != 1 && dragTo != -1 && dragFrom != dragTo){
//            reallyMoved(dragFrom,dragTo)
//        }
//
//        dragFrom = -1
//        dragTo = -1
//    }
}