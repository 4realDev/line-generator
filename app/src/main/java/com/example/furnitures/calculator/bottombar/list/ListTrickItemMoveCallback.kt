package com.example.furnitures.calculator.bottombar.list

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ListTrickItemMoveCallback(private val adapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback() {

    var dragFrom = -1
    var dragTo = -1

    override fun isLongPressDragEnabled(): Boolean = false
    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START
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

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    // Returns ItemTouchUIUtil that is used by ItemTouchHelper.Callback for visual changes on Views in response to user interactions

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        /*
        val foregroundView: View = viewHolder.itemView.findViewById(R.id.viewForeground)
        getDefaultUIUtil().clearView(foregroundView)
        */

        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            adapter.dragFinished(dragFrom, dragTo)
            Log.d("DEBUGG", "DRAG FROM: ${dragFrom}, DRAG TO: ${dragTo}")
        }
        dragFrom = -1
        dragTo = -1
    }

    /*
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(viewHolder != null ) {
            val foregroundView: View = viewHolder.itemView.findViewById(R.id.viewForeground)
            getDefaultUIUtil().onSelected(foregroundView)

        }
    }
    */

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val foregroundView: View = viewHolder.itemView.findViewById(com.example.furnitures.R.id.viewForeground)
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    /*
    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView: View = viewHolder!!.itemView.findViewById(R.id.viewForeground)
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }
    */

    /*
    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }
    */
}

// MoveCallback bekommt den ItemTouchHelperAdapter durch den Konstruktor
// Über die functions vom Interface HelperAdapter,
// ListTrickAdapter implementiert das HelperAdapter Interface und überschreibt die functionens
// greift MoveCallback auf die functionen des ListTrickAdapters zu