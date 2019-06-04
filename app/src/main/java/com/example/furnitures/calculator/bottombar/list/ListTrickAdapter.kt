package com.example.furnitures.calculator.bottombar.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.R
import com.example.furnitures.calculator.trick.FurnitureViewState

class ListTrickAdapter(
    private var furnitureModificationListener: FurnitureModificationListener? = null,
    private var mDragStartListener: OnStartDragListener? = null
) : RecyclerView.Adapter<ListTrickAdapter.FurnitureHolder>(), ItemTouchHelperAdapter {

    private var draggingItem: FurnitureViewState? = null
    private var targetItem: FurnitureViewState? = null

    private val furnitureItemDiffer by lazy {
        AsyncListDiffer<FurnitureViewState>(
            // Überschreiben des ListUpdateCallbacks
            // Wofür ist Offset gut?
            this,
            furnitureCallback
        )
    }

    private val furnitureCallback = object : DiffUtil.ItemCallback<FurnitureViewState>() {
        override fun areItemsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_trick, parent, false)
        val viewHolder = FurnitureHolder(itemView)
        return viewHolder
    }

    // suppress warning - warning cause no optimation for blind people
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: FurnitureHolder, position: Int) {
        val item = getFurnitureItem(position)
        holder.title?.setText(item!!.name)
        holder.count?.text = String.format("%02d", position + 1)
        holder.handle?.setOnTouchListener { handle, event ->
            if (event.action == MotionEvent.ACTION_DOWN) mDragStartListener?.onStartDrag(holder)
            false
        }
    }

    inner class FurnitureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture__title)
        val count: TextView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture_count)
        val handle: ImageView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture_handle)
    }

    // Anstatt als Class als Object anlegen
    // Direkte Implementierung des Interfaces bei Erstellung des Adapters


    private fun getFurnitureItem(position: Int): FurnitureViewState? {
        return furnitureItemDiffer.currentList.getOrNull(position)
    }

    fun onFurnitureItemsUpdate(newData: List<FurnitureViewState>) {
        furnitureItemDiffer.submitList(newData)
    }

    override fun getItemCount(): Int {
        return furnitureItemDiffer.currentList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (draggingItem == null) draggingItem = getFurnitureItem(fromPosition)
        targetItem = getFurnitureItem(toPosition)

        // Update Adapter list
        val list = furnitureItemDiffer.currentList.toMutableList()
        val removed = list.removeAt(fromPosition)
        list.add(toPosition, removed)
        furnitureItemDiffer.submitList(list)
        Log.d(ListTrickAdapter::class.java.name.toString(), "toAdapterPosition: $toPosition fromAdapterPosition: $fromPosition")
    }

    override fun onItemDismiss(position: Int) {
        getFurnitureItem(position)?.let { furnitureModificationListener?.onItemRemove(it) }
    }

    override fun dragFinished(fromIndex: Int, toIndex: Int) {
        furnitureModificationListener?.onItemMove(fromIndex, toIndex)
        draggingItem = null
        targetItem = null
    }

    interface FurnitureModificationListener {
        fun onItemRemove(furnitureItem: FurnitureViewState)
        fun onItemMove(fromIndex: Int, toIndex: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }
}
