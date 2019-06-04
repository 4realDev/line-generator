package com.example.furnitures.calculator.bottombar.selection

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.list.ItemTouchHelperAdapter
import com.example.furnitures.calculator.trick.FurnitureViewState

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class ListTrickAdapter(
    private var furnitureModificationListener: ListTrickAdapter.FurnitureModificationListener
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

    private val furnitureCallback = object : DiffUtil.ItemCallback<FurnitureViewState>(){
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

    override fun onBindViewHolder(holder: FurnitureHolder, position: Int) {
        val item = getFurnitureItem(position)
        holder.title?.setText(item!!.name)
        holder.count?.text = String.format("%02d", position + 1)
    }

    inner class FurnitureHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(R.id.list_item_furniture__title)
        val count: TextView? = itemView.findViewById(R.id.list_item_furniture_count)
    }

    //  Anstatt als Class als Object anlegen
    // sodass man furnitureCallback furnitureItemDiffer übergeben kann als AsyncDifferConfig


    private fun getFurnitureItem(position: Int): FurnitureViewState? {
        return furnitureItemDiffer.currentList.getOrNull(position)
    }

    fun onFurnitureItemsUpdate(newData: List<FurnitureViewState>){
        furnitureItemDiffer.submitList(newData)
    }

    override fun getItemCount(): Int {
        return furnitureItemDiffer.currentList.size
    }


//    class DiffCallback : DiffUtil.ItemCallback<FurnitureViewState>() {
//        override fun areItemsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
//            return oldItem == newItem
//        }
//    }


    // Implementierung des Interfaces
    // Aufruf der Funktionen über ListTrickItemMoveCallback bei den Listeners

    override fun onItemMove(fromPosition: Int, toPosition: Int) {

        if(draggingItem == null) draggingItem = getFurnitureItem(fromPosition)
        targetItem = getFurnitureItem(toPosition)

        // Update Adapter list
        val list = furnitureItemDiffer.currentList.toMutableList()
        val removed = list.removeAt(fromPosition)
        list.add(toPosition, removed)

        furnitureItemDiffer.submitList(list)

        Log.d("DEBUGG", "toAdapterPosition: $toPosition fromAdapterPosition: $fromPosition")
    }

    override fun onItemDismiss(position: Int) {
        getFurnitureItem(position)?.let { furnitureModificationListener.onItemRemove(it)}
    }

    override fun dragFinished(fromIndex: Int, toIndex: Int) {
        furnitureModificationListener.onItemMove(fromIndex, toIndex)
        draggingItem = null
        targetItem = null
    }

    interface FurnitureModificationListener {
        fun onItemRemove(furnitureItem: FurnitureViewState)
        fun onItemMove(fromIndex: Int, toIndex: Int)
    }
}
