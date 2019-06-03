package com.example.furnitures.calculator.bottombar.list

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ListTrickItemMoveCallback(private val adapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback() {

    var dragFrom = -1
    var dragTo = -1

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        if (dragFrom == -1) {
            dragFrom = fromPosition
        }
        dragTo = toPosition
        // function to update the AdapterList/ UI
        // ruft das Interface auf, welches von einem Adapter implementiert wird
        adapter.onItemMove(fromPosition, toPosition)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            adapter.dragFinished(dragFrom, dragTo)
            Log.d("DEBUGG", "DRAG FROM: ${dragFrom}, DRAG TO: ${dragTo}")
        }
        dragFrom = -1
        dragTo = -1
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}

// MoveCallback bekommt den ItemTouchHelperAdapter durch den Konstruktor
// Über die functions vom Interface HelperAdapter,
// ListTrickAdapter implementiert das HelperAdapter Interface und überschreibt die functionens
// greift MoveCallback auf die functionen des ListTrickAdapters zu